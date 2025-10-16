package live.videosdk.call_trigger.kotlin.example

interface MeetingIdCallBack {
    fun onMeetingIdReceived(meetingId: String, token: String)
}
