package com.example.zoomkotlindemo

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import us.zoom.sdk.MeetingServiceListener
import us.zoom.sdk.MeetingStatus
import us.zoom.sdk.ZoomError
import us.zoom.sdk.ZoomSDKInitializeListener


class MainActivity : AppCompatActivity(), ZoomSDKInitializeListener, MeetingServiceListener {

    private lateinit var  mZoomHelper: ZoomInitializeHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnJoin).setOnClickListener { autoJoinMeeting()}

        ZoomInitializeHelper.initialize(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        JoinMeetingHelper.removeMeetingServiceListener(this)

    }

    companion object {
        const val TAG = "MainActivity"
    }

    private fun showMsg(msg: String) {
        findViewById<TextView>(R.id.txtMsg).text = msg
    }

    private fun enableJoinMeeting(value: Boolean) {
        findViewById<Button>(R.id.btnJoin).isEnabled = value
    }

    private fun autoJoinMeeting() {

        // secrets.xmlから取得。
        val meetingNo = getString(R.string.ZOOM_MTG_NUMBER)
        val meetingPwd = getString(R.string.ZOOM_MTG_PASSWORD)
        val displayName = "テストユーザー1"

        enableJoinMeeting(false)
        JoinMeetingHelper.addMeetingServiceListener(this)
        JoinMeetingHelper.joinMeetingWithNumber(this, meetingNo, meetingPwd, displayName)
    }

    override fun onZoomSDKInitializeResult(errCd: Int, internalErrCd: Int) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=$errCd, internalErrorCode=$internalErrCd")

        val msg =
            if (errCd != ZoomError.ZOOM_ERROR_SUCCESS) {
                val s = getString(R.string.initialize_err)
                "$s Error: $errCd, internalErrorCode=$internalErrCd"
            } else {
                getString(R.string.initialize_done)
            }

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        showMsg(msg)

        if (errCd == ZoomError.ZOOM_ERROR_SUCCESS) {
            enableJoinMeeting(true)
        }
    }

    override fun onZoomAuthIdentityExpired() {
        Log.e(MainActivity.TAG, "onZoomAuthIdentityExpired")
    }

    override fun onMeetingStatusChanged(
        meetingStatus: MeetingStatus,
        errorCode: Int,
        internalErrorCode: Int
    ) {

        Log.d(
            MainActivity.TAG,
            "onMeetingStatusChanged $meetingStatus:$errorCode:$internalErrorCode"
        )

        val msg = when (meetingStatus) {
            MeetingStatus.MEETING_STATUS_CONNECTING -> "ミーティングに接続中..."
            MeetingStatus.MEETING_STATUS_INMEETING -> "ミーティングに参加しています"
            MeetingStatus.MEETING_STATUS_DISCONNECTING -> "ミーティングから退出中..."
            MeetingStatus.MEETING_STATUS_IDLE -> "ミーティングから退出しました"
            MeetingStatus.MEETING_STATUS_WAITINGFORHOST -> "ホストを待っています..."
            MeetingStatus.MEETING_STATUS_RECONNECTING -> "再接続中..."
            MeetingStatus.MEETING_STATUS_FAILED -> "ミーティングに参加出来ませんでした"
            MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM -> "ウェイティングルームで待機中"
            MeetingStatus.MEETING_STATUS_WEBINAR_PROMOTE -> "WEBINAR_PROMOTE"
            MeetingStatus.MEETING_STATUS_WEBINAR_DEPROMOTE -> "WEBINAR_DEPROMOTE"
            MeetingStatus.MEETING_STATUS_UNKNOWN -> "ステータスが不明です"
        }

        showMsg(msg)

        val enableJoin = meetingStatus == MeetingStatus.MEETING_STATUS_IDLE
                || meetingStatus == MeetingStatus.MEETING_STATUS_FAILED
                || meetingStatus == MeetingStatus.MEETING_STATUS_UNKNOWN
        enableJoinMeeting(enableJoin)
    }



}