package live.videosdk.call_trigger.kotlin.example.Services

import android.content.Intent
import android.telecom.Connection
import android.telecom.ConnectionRequest
import android.telecom.ConnectionService
import android.telecom.DisconnectCause
import android.telecom.PhoneAccountHandle
import android.telecom.TelecomManager
import android.util.Log
import live.videosdk.call_trigger.kotlin.example.MainActivity
import live.videosdk.call_trigger.kotlin.example.Meeting.MeetingActivity
import live.videosdk.call_trigger.kotlin.example.Network.NetworkCallHandler


class CallConnectionService : ConnectionService() {
    var callerID: String? = null
    val obj = NetworkCallHandler()

    override fun onCreateIncomingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Connection {
        // Create a connection for the incoming call
        val connection: Connection = object : Connection() {
            override fun onAnswer() {
                super.onAnswer()
                //getting videosdk info
                val extras = request.extras
                val meetingId = extras.getString("meetingId")
                val token = extras.getString("token")
                callerID = extras.getString("callerID")
                obj.updateCall("ACCEPTED")
                // Start the meeting activity with the extracted data
                val intent = Intent(
                    applicationContext,
                    MeetingActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("meetingId", meetingId)
                intent.putExtra("token", token)
                startActivity(intent)

                //update
                setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
                destroy()
            }

            override fun onReject() {
                super.onReject()
                val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                //update
                obj.updateCall("REJECTED")
                setDisconnected(DisconnectCause(DisconnectCause.LOCAL))
                destroy()
            }
        }

        // Set call address
        connection.setAddress(request.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setCallerDisplayName(callerID, TelecomManager.PRESENTATION_ALLOWED)
        connection.setInitializing() // Indicates that the call is being set up
        connection.setActive() // Activate the call

        return connection
    }

    override fun onCreateOutgoingConnection(
        connectionManagerPhoneAccount: PhoneAccountHandle,
        request: ConnectionRequest
    ): Connection {
        // Create a connection for the outgoing call
        val connection: Connection = object : Connection() {}
        connection.setAddress(request.address, TelecomManager.PRESENTATION_ALLOWED)
        connection.setActive()

        return connection
    }
}
