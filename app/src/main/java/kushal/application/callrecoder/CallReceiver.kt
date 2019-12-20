package kushal.application.callrecoder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import java.io.File

class CallReceiver : BroadcastReceiver() {

    lateinit var file: File
    lateinit var recorder: MediaRecorder
    val path = "/sdcard/downloads"


    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {
        context!!.startService(Intent(context, RecordService::class.java))

    }

}


//        var isCalling = false
//        val pref = context!!.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
//
//        val service = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//        service.listen(object : PhoneStateListener() {
//            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
//                super.onCallStateChanged(state, phoneNumber)
//
//                file =
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                val date = Date()
//                val sdf = "dd-MM-yy-hh-mm-ss"
//                val format = DateFormat.format(sdf, date.time)
//
//                recorder = MediaRecorder()
//                recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL)
//                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                recorder.setOutputFile("${file.absoluteFile}/${format}recorder.3gp")
//                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//
//
//                if (TelephonyManager.CALL_STATE_RINGING == state) {
//
//                    val i = Intent(context, DialogAct::class.java)
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                    i.putExtra("number",intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
//                    context.startActivity(i)
//                }
//
//                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
//
//                    isCalling = true
//
//                    if (pref.getBoolean("record", false)) {
//                        recordStuff()
//                        Toast.makeText(context, "started recorder", Toast.LENGTH_SHORT).show()
//                    }
//
//                }
//
//                if (state == TelephonyManager.CALL_STATE_IDLE) {
////                    if (isCalling) { // after coming back from call
//
//                    if (pref.getBoolean("record", false)) {
//
//                        Toast.makeText(context, "saved recording", Toast.LENGTH_SHORT).show()
//
//                        stopRecorder()
//
//                        pref.edit().putBoolean("record", false).apply()
//                    }
//
//                    isCalling = false
//
////                        val i = Intent(context, ClearStack::class.java)
////                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////                        i.flags =
////                            Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY
////                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                        context.startActivity(i)
//
////                    }
//                }
//
//            }
//        }, PhoneStateListener.LISTEN_CALL_STATE)
//
//
//    }
//
//    private fun stopRecorder() {
//
//        try {
//            recorder.stop()
//            recorder.reset()
//            recorder.release()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private fun recordStuff() {
//
//        try {
//            recorder.prepare()
//            recorder.start()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }
