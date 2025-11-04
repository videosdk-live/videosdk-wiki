# 🚀 Video SDK for IoT

[![Documentation](https://img.shields.io/badge/Read-Documentation-blue)](https://docs.videosdk.live/flutter/guide/video-and-audio-calling-api-sdk/concept-and-architecture)
[![Discord](https://img.shields.io/discord/876774498798551130?label=Join%20on%20Discord)](https://discord.gg/bGZtAbwvab)
[![Register](https://img.shields.io/badge/Contact-Know%20More-blue)](https://app.videosdk.live/signup)


At Video SDK, we’re building tools to help developers bring **real-time collaboration** to IoT and embedded devices. With the IoT SDK, you can integrate **live audio communication, meeting management, device-to-cloud connectivity, and session handling** directly into ESP32.


### 🥳 Get **10,000 minutes free** every month! **[Try it now!](https://app.videosdk.live/signup)**

## 📚 **Table of Contents**

- [⚡ **Quick Setup**](#-quick-setup)
- [🔧 **Prerequisites**](#-prerequisites)
- [📦 **Running the Quick Start example**](#-running-the-quick-start-example)
- [🧠 **Key Concepts**](#-key-concepts)
- [🔑 **Token Generation**](#-token-generation)
- [📝 **VideoSDK's Documentation**](#-documentation)
- [💬 **Join Our Community**](#-join-our-community)



## ⚡ Quick Setup 

1. Sign up on [VideoSDK](https://app.videosdk.live/) to grab your API Key and Secret.

## 🛠 Prerequisites

- Python >= 3.11
- A valid [Video SDK Account](https://app.videosdk.live/)

## 📦 Running the Quick Start example

Follow these steps to run the Quick Start example:

### 1. Configure ESP-IDF enviorment 

👉 For setting up **ESP-IDF**, follow only **Step 1** from the from videosdk's [documentation](https://docs.videosdk.live/iot/guide/video-and-audio-calling-api-sdk/quickstart/quick-start#step-1-setup-for-esp-idf).

⚠️ Inside Step 1, you **do not need to run the project creation commands** — 
```
// Create a esp-idf project
cd ~/esp
idf.py create-project your-project-name
cd your-project-name
```
once the tools and environment are set up, jump directly to **Step 2 in this README**.

### 2. Clone the sample project

Clone the repository to your local environment.

```js
git clone https://github.com/videosdk-live/videosdk-rtc-iot-sdk-example.git
```
### 3: Add IoT SDK component

To integrate the IoT SDK component, clone the repository below.

```
git clone https://github.com/videosdk-live/IoTSdk
```

After cloning, copy the component's local path and provide it in the `idf_component.yml` file as shown below.
```
dependencies:
  IoTSdk: 
    path: // Full local path of the component
```

### 4: Configure Dependencies

Open your `idf_component.yml` file and add the required dependencies. Place them below your existing ones:

```
  // Add the following dependencies after your existing ones
  mdns: '*'
  espressif/esp_audio_codec: ~2.3.0
  espressif/esp_codec_dev: ~1.3.4
  espressif/esp_audio_effects: ~1.1.0
  protocol_examples_common:
    path: ${IDF_PATH}/examples/common_components/protocol_examples_common
    ## Required IDF version
  idf:
    version: '>=4.1.0'
  sepfy/srtp: ^2.3.0
```

### 5. Generate a token from [generate videosdk token](https://app.videosdk.live) and modify token variable in `quick_start.c` file
```
const char *token = "GENRERATED_TOKEN"; // Your VideoSDK Authentication token
```

#### 6. Configure Publisher and Subscriber IDs

- You can pass your your Ids or use any random ID for `your-publisherId` and `your-subscriberId`.  
- For `your-subscriberToId`, pass the participant’s ID whose audio you want to subscribe to.

```
result_t result_publish = startPublishAudio("your-publisherId");

// Start subscribing to an audio stream
result_t result_susbcribe = startSubscribeAudio("your-subscriberId", "your-subscriberToId");

```

### 6. Build & Flash Project

Configure, build, and flash the firmware onto your ESP32 board. This compiles the code, applies the configurations, and uploads it to your device.
```
1. <!-- Run this command to set your board as the target-->
idf.py set-target esp32-s3

2. <!-- Run this command to do menuconfig -->

idf.py menuconfig  

         a. Inside the component config:
                |
                |———> mbedtls
                      | ——>Enable Support DTLS    <!-- It enables 3 way handshake  -->
                      | ——>Enable Support TLS      <!-- It enables 3 way handshake  -->
          And click S to save and again enter       

          b. Inside Example Connection Configuration:
                |
                |———> WIFI SSID         <!-- replace it with your WiFi name  -->
                |———> WIFI Password     <!-- replace it with your WiFi password -->
          And click S to save and again enter 

          c. Inside the Partition table :
                |
                |———> Partition table (custom partition table CSV)        
                      |———> Enable Custom partition table CSV

          d. Adjust the flash size inside Serial flasher config 
             (because some boards have limited flash memory)
                | ——> flash size: 2MB (esp32s3 XIAO sense)
                | ——> flash size: 4MB (esp32s3 qorvo2 v3.1)
          And click S to save and again enter

          e. Inside the Set Microcontroller : 
                | ——>**Audio hardware board (example : ESP32-S3-Korvo-2)**
                      | ——> Select your board name
                              |———> ESP32-S3-XIAO       
                              |———> ESP32- ESP32-S3-Korvo
          And click S to save and again enter

3. <!-- Run this command to build the project  -->
idf.py build

4. <!-- Run this command to flash the project to your microcontroller -->
idf.py flash monitor 
```

## 🧠 Key Concepts

Understand the core components of our SDK:

- `Meeting` - A Meeting represents Real-time audio communication.
- `Sessions` - A particular duration you spend in a given meeting is referred as a session, you can have multiple sessions of a specific meetingId.
- `Participant` - A participant refers to anyone attending the meeting session. The `local participant` represents yourself (You), while all other attendees are considered `remote participants`.
- `AudioStream` - A stream refers to audio media content that is published by either the `local participant` or `remote participants`.


## Documentation

For more details, check out the [VideoSDK Documentation](https://docs.videosdk.live/iot/guide/video-and-audio-calling-api-sdk/concept-and-architecture)


## 📖 Examples

- [**Prebuilt Example**](https://github.com/videosdk-live/videosdk-rtc-prebuilt-examples)
- [**JavaScript SDK Example**](https://github.com/videosdk-live/videosdk-rtc-javascript-sdk-example)
- [**React SDK Example**](https://github.com/videosdk-live/videosdk-rtc-react-sdk-example.git)
- [**React Native SDK Example**](https://github.com/videosdk-live/videosdk-rtc-react-native-sdk-example)
- [**Android Java SDK Example**](https://github.com/videosdk-live/videosdk-rtc-android-java-sdk-example)
- [**Android Kotlin SDK Example**](https://github.com/videosdk-live/videosdk-rtc-android-kotlin-sdk-example)
- [**iOS SDK Example**](https://github.com/videosdk-live/videosdk-rtc-ios-sdk-example)


## 🤝 Join Our Community

- **[Discord](https://discord.gg/Gpmj6eCq5u)**: Engage with the Video SDK community, ask questions, and share insights.
- **[X](https://x.com/video_sdk)**: Stay updated with the latest news, updates, and tips from Video SDK.
