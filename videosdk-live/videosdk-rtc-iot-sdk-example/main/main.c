#include <stddef.h>
#include <stdint.h>
#include <stdio.h>
#include <string.h>
#include <sys/param.h>
#include <sys/time.h>
#include "esp_event.h"
#include "esp_log.h"
#include "esp_mac.h"
#include "esp_netif.h"
#include "esp_partition.h"
#include "esp_system.h"
#include "freertos/FreeRTOS.h"
#include "mdns.h"
#include "nvs_flash.h"
#include "protocol_examples_common.h"
#include "videosdk.h"

static const char *TAG = "VideoSDK"; // Logging tag
const char *token = "Your-Token";    // Your VideoSDK Authentication token
char *meetingId;
// Task to create a meeting using the VideoSDK
static void meeting_task(void *pvParameters)
{

    create_meeting_result_t result = create_meeting(token); // Create meeting using IoT SDK

    if (result.room_id)
    {
        ESP_LOGI(TAG, "Created meeting roomId = %s", result.room_id);
        meetingId = result.room_id; // Set global meetingId`
        free(result.room_id);       // Free allocated memory
    }
    else
    {
        ESP_LOGE(TAG, "Failed to create meeting");
    }

    ESP_LOGI(TAG, "meeting_task finished, deleting self");
    vTaskDelete(NULL); // Delete task after completion
}

void app_main(void)
{
    static char deviceid[32] = {0}; // Buffer to hold device ID
    uint8_t mac[8] = {0};           // MAC address buffer

    ESP_LOGI(TAG, "[APP] Startup..");
    ESP_LOGI(TAG, "[APP] Free memory: %d bytes", esp_get_free_heap_size());
    ESP_LOGI(TAG, "[APP] IDF version: %s", esp_get_idf_version());

    // Configure logging levels
    esp_log_level_set("*", ESP_LOG_INFO);
    esp_log_level_set("esp-tls", ESP_LOG_VERBOSE);
    esp_log_level_set("MQTT_CLIENT", ESP_LOG_VERBOSE);
    esp_log_level_set("MQTT_EXAMPLE", ESP_LOG_VERBOSE);
    esp_log_level_set("TRANSPORT_BASE", ESP_LOG_VERBOSE);
    esp_log_level_set("TRANSPORT", ESP_LOG_VERBOSE);
    esp_log_level_set("OUTBOX", ESP_LOG_VERBOSE);

    // Initialize system components
    ESP_ERROR_CHECK(nvs_flash_init());
    ESP_ERROR_CHECK(esp_netif_init());
    ESP_ERROR_CHECK(esp_event_loop_create_default());
    ESP_ERROR_CHECK(example_connect());

    // Generate unique device ID from MAC address of your esp
    if (esp_read_mac(mac, ESP_MAC_WIFI_STA) == ESP_OK)
    {
        sprintf(deviceid, "esp32-%02x%02x%02x%02x%02x%02x",
                mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);
        ESP_LOGI(TAG, "Device ID: %s", deviceid);
    }

    // Create a task to handle meeting creation
    BaseType_t ok = xTaskCreate(meeting_task, "meeting_task", 16384, (void *)token, 5, NULL);
    if (ok != pdPASS)
    {
        ESP_LOGE(TAG, "Failed to create meeting_task");
    }

    // Initialize VideoSDK configuration
    init_config_t init_cfg = {
        .meetingID = meetingId,
        .token = token,
        .displayName = "ESP32_Device",
        .audioCodec = AUDIO_CODEC_OPUS,
    };

    result_t init_result = init(&init_cfg); // Initialize SDK with config
    printf("Result: %d\n", init_result);

    // Start publishing audio stream
    result_t result_publish = startPublishAudio("Your-PublisherId"); // change the publisherId
    printf("Result:%d\n", result_publish);
    // Start subscribing to an audio stream
    result_t result_susbcribe = startSubscribeAudio("Your-SubscriberId", "Your-SubscribeToId"); // change the SubscriberId and SubscribeToId
    printf("Result:%d\n", result_susbcribe);

    // Leave the meeting
    // result_t result_leave = leave();

    // Keep main loop alive
    while (1)
    {
        vTaskDelay(pdMS_TO_TICKS(10));
    }
}
