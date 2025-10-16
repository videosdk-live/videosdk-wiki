package live.videosdk.call_trigger.example.Services;

import android.content.pm.PackageManager;
import android.telecom.Call;
import android.telecom.InCallService;
import android.telecom.TelecomManager;

import androidx.core.app.ActivityCompat;

public class MyInCallService extends InCallService {

    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);
        call.registerCallback(new Call.Callback() {
            @Override
            public void onStateChanged(Call call, int state) {
                super.onStateChanged(call, state);
                if (state == Call.STATE_ACTIVE) {
                    // Handle the active call state
                }
            }
        });

        // Bring up the default UI for managing the call
        setUpDefaultCallUI(call);
    }

    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
    }

    private void setUpDefaultCallUI(Call call) {
        // Start the default in-call UI
        TelecomManager telecomManager = (TelecomManager) getSystemService(TELECOM_SERVICE);
        if (telecomManager != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            telecomManager.showInCallScreen(true);
        }
    }
}
