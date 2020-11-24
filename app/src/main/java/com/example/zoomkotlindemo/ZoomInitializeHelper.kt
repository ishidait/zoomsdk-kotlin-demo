package com.example.zoomkotlindemo

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import us.zoom.sdk.*

object ZoomInitializeHelper {

    private const val TAG = "ZoomInitializeHelper"
    private lateinit var  mZoomSDK: ZoomSDK

    fun initialize(context: Context, zoomSDKInitializeListener: ZoomSDKInitializeListener) {

        // secrets.xmlから取得。
        val zoomAppKey = context.getString(R.string.ZOOMSDK_APP_KEY)
        val zoomAppSecret = context.getString(R.string.ZOOMSDK_APP_SECRET)

        val params = ZoomSDKInitParams().apply {
            domain = "https://zoom.us"      // Required
            appKey = zoomAppKey
            appSecret = zoomAppSecret
            enableGenerateDump = true
            logSize = 5
            videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack
            enableLog = true // Optional for debugging
        }

        mZoomSDK = ZoomSDK.getInstance()
        mZoomSDK.initialize(context, zoomSDKInitializeListener, params)
    }

}