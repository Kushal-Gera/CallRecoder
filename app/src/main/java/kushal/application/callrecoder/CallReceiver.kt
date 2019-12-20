package kushal.application.callrecoder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class CallReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context!!.startForegroundService(Intent(context, RecordService::class.java))
            } else
                context!!.startService(Intent(context, RecordService::class.java))
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

}
