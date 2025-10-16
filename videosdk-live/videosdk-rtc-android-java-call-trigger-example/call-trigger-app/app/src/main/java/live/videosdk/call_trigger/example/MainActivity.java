package live.videosdk.call_trigger.example;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;
import java.util.Random;

import live.videosdk.call_trigger.example.FirebaseDatabase.DatabaseUtils;
import live.videosdk.call_trigger.example.Network.NetworkUtils;
import live.videosdk.call_trigger.example.Network.NetworkCallHandler;
import live.videosdk.rtc.android.lib.Async;

public class MainActivity extends AppCompatActivity {

    private EditText callerIdInput;
    private TextView myId;
    ImageView copyIcon;
    public static String myCallId = String.valueOf(10000000 + new Random().nextInt(90000000));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID);
        }
        myId = findViewById(R.id.txt_callId);
        callerIdInput = findViewById(R.id.caller_id_input);

        copyIcon = findViewById(R.id.copyIcon);
        Button callButton = findViewById(R.id.call_button);
        myId.setText(myCallId);

        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copied text", myCallId);

        copyIcon.setOnClickListener(v -> {
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        NetworkUtils networkUtils = new NetworkUtils();
        networkUtils.createMeeting(new MeetingIdCallBack() {
            @Override
            @Async
            public void onMeetingIdReceived(String meetingId, String token) {
                MainApplication.setMeetingId(meetingId);
            }
        });


        //Firebase Notification
        NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed Successfully";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

       //Firebase Database Actions
        DatabaseUtils databaseUtils = new DatabaseUtils();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener( task -> {
            NetworkCallHandler.FcmToken = task.getResult();
            DatabaseUtils.FcmToken= task.getResult();
            databaseUtils.sendUserDataToFirebase(databaseReference);
        });

        //telecom Api
        registerPhoneAccount();

        callButton.setOnClickListener(v -> {
            String callerNumber = callerIdInput.getText().toString();

            if (callerNumber.length() == 8) {
                databaseUtils.retrieveUserData(databaseReference, callerNumber);
            } else {
                Toast.makeText(MainActivity.this, "Please input the correct caller ID", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 133) {
            boolean allPermissionsGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                registerPhoneAccount();
            } else {
                Toast.makeText(this, "Permissions are required for call management", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void registerPhoneAccount() {

    PhoneAccount phoneAccount = PhoneAccount.builder(MainApplication.phoneAccountHandle, "VideoSDK")
            .setCapabilities(PhoneAccount.CAPABILITY_CALL_PROVIDER)
            .build();

    MainApplication.telecomManager.registerPhoneAccount(phoneAccount);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        int checkAccount=0;
        List<PhoneAccountHandle> list = MainApplication.telecomManager.getCallCapablePhoneAccounts();
        for (PhoneAccountHandle handle:
             list) {
            if(handle.getComponentName().getClassName().equals("live.videosdk.call_trigger.example.Services.CallConnectionService"))
            {
                checkAccount++;
                break;
            }
        }

        if(checkAccount == 0) {
            Intent intent = new Intent(TelecomManager.ACTION_CHANGE_PHONE_ACCOUNTS);
            startActivity(intent);
        }
    }

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.POST_NOTIFICATIONS
    };

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }
}
