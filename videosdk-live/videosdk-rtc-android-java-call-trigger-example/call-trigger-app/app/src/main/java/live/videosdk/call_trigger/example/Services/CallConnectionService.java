package live.videosdk.call_trigger.example.Services;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Connection;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.PhoneAccountHandle;
import android.telecom.ConnectionRequest;
import android.telecom.TelecomManager;
import android.util.Log;

import live.videosdk.call_trigger.example.MainActivity;
import live.videosdk.call_trigger.example.Meeting.MeetingActivity;
import live.videosdk.call_trigger.example.Network.NetworkCallHandler;

public class CallConnectionService extends ConnectionService {

String callerID;

    @Override
    public Connection onCreateIncomingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        // Create a connection for the incoming call
        Connection connection = new Connection() {
            @Override
            public void onAnswer() {
                super.onAnswer();
               //getting videosdk info
                Bundle extras = request.getExtras();
                String meetingId = extras.getString("meetingId");
                String token = extras.getString("token");
                callerID = extras.getString("callerID");

                // Start the meeting activity with the extracted data
                Intent intent = new Intent(getApplicationContext(), MeetingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("meetingId", meetingId);
                intent.putExtra("token", token);
                startActivity(intent);
                NetworkCallHandler.updateCall("ACCEPTED");

                //update
                setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
                destroy();
            }

            @Override
            public void onReject() {
                super.onReject();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //update
                NetworkCallHandler.updateCall("REJECTED");

                setDisconnected(new DisconnectCause(DisconnectCause.LOCAL));
                destroy();
            }

        };

        // Set call address
        connection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED);
        connection.setCallerDisplayName(callerID,TelecomManager.PRESENTATION_ALLOWED);
        connection.setInitializing();  // Indicates that the call is being set up
        connection.setActive();  // Activate the call

        return connection;
    }

    @Override
    public Connection onCreateOutgoingConnection(PhoneAccountHandle connectionManagerPhoneAccount, ConnectionRequest request) {
        // Create a connection for the outgoing call
        Connection connection = new Connection(){};
        connection.setAddress(request.getAddress(), TelecomManager.PRESENTATION_ALLOWED);
        connection.setActive();

        return connection;
    }
}
