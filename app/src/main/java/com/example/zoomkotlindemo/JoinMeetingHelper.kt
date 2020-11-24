package com.example.zoomkotlindemo

import android.content.Context
import com.example.zoomkotlindemo.ZoomMeetingUISettingHelper.joinMeetingOptions
import us.zoom.sdk.*

object ZoomMeetingUISettingHelper {
    @kotlin.jvm.JvmStatic
    val meetingOptions = JoinMeetingOptions()

    @kotlin.jvm.JvmStatic
    val joinMeetingOptions: JoinMeetingOptions
        get() {
            val opts = JoinMeetingOptions()
            fillMeetingOption(opts)
            opts.no_audio = meetingOptions.no_audio
            return opts
        }

    private fun fillMeetingOption(opts: MeetingOptions): MeetingOptions {
        opts.no_driving_mode = meetingOptions.no_driving_mode
        opts.no_invite = meetingOptions.no_invite
        opts.no_meeting_end_message = meetingOptions.no_meeting_end_message
        opts.no_meeting_error_message = meetingOptions.no_meeting_error_message
        opts.no_titlebar = meetingOptions.no_titlebar
        opts.no_bottom_toolbar = meetingOptions.no_bottom_toolbar
        opts.no_dial_in_via_phone = meetingOptions.no_dial_in_via_phone
        opts.no_dial_out_to_phone = meetingOptions.no_dial_out_to_phone
        opts.no_disconnect_audio = meetingOptions.no_disconnect_audio
        opts.no_share = meetingOptions.no_share
        opts.no_video = meetingOptions.no_video
        opts.meeting_views_options = meetingOptions.meeting_views_options
        opts.invite_options = meetingOptions.invite_options
        opts.participant_id = meetingOptions.participant_id
        opts.custom_meeting_id = meetingOptions.custom_meeting_id
        opts.no_unmute_confirm_dialog = meetingOptions.no_unmute_confirm_dialog
        opts.no_webinar_register_dialog = meetingOptions.no_webinar_register_dialog
        return opts
    }
}


object JoinMeetingHelper {
    private const val TAG = "JoinMeetingHelper"
    private const val DISPLAY_NAME = "ZoomUS_SDK"

    private val mZoomSDK: ZoomSDK = ZoomSDK.getInstance()

    fun joinMeetingWithNumber(
        context: Context?,
        meetingNo: String,
        meetingPassword: String?,
        displayName: String?
    ): Int {
        val ret = -1
        val meetingService = mZoomSDK.meetingService ?: return ret

        mZoomSDK.zoomUIService.enableMinimizeMeeting(false)
        mZoomSDK.meetingSettingsHelper.enable720p(false)
        mZoomSDK.meetingSettingsHelper.enableShowMyMeetingElapseTime(true)

        val opts = joinMeetingOptions
        val params = JoinMeetingParams()
        params.displayName = displayName ?: DISPLAY_NAME
        params.meetingNo = meetingNo
        params.password = meetingPassword

        return meetingService.joinMeetingWithParams(context, params, opts)
    }

    fun addMeetingServiceListener(meetingServiceListener: MeetingServiceListener) {
        mZoomSDK.meetingService.addListener(meetingServiceListener)
    }

    fun removeMeetingServiceListener(meetingServiceListener: MeetingServiceListener) {
        mZoomSDK.meetingService.removeListener(meetingServiceListener)
    }

}