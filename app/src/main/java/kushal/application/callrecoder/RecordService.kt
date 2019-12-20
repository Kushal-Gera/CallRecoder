package kushal.application.callrecoder

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Environment
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.text.format.DateFormat
import android.widget.Toast
import java.io.File
import java.util.*

class RecordService : Service() {

    lateinit var file: File
    lateinit var recorder: MediaRecorder
    val path = "/sdcard/downloads"
    val context = this

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var isCalling = false

        val pref = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        val service = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        service.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)

                file =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val date = Date()
                val sdf = "dd-MM-yy--hh:mm:ss"
                val format = DateFormat.format(sdf, date.time)

                recorder = MediaRecorder()
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                recorder.setOutputFile("${file.absoluteFile}/${format}recorder.3gp")
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)


                if (TelephonyManager.CALL_STATE_RINGING == state) {

                    val i = Intent(context, DialogAct::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    i.putExtra("number",intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
                    context.startActivity(i)
                }

                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                    isCalling = true

                    if (pref.getBoolean("record", false)) {
                        recordStuff()
                        Toast.makeText(context, "started recorder", Toast.LENGTH_SHORT).show()
                    }

                }

                if (state == TelephonyManager.CALL_STATE_IDLE) {
//                    if (isCalling) { // after coming back from call

                    if (pref.getBoolean("record", false)) {

                        stopRecorder()

                        Toast.makeText(context, "saved recording", Toast.LENGTH_SHORT).show()

                        pref.edit().putBoolean("record", false).apply()
                    }

                    isCalling = false
//                    }

//                        val i = Intent(context, ClearStack::class.java)
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                        i.flags =
//                            Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        context.startActivity(i)

                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE)

        return super.onStartCommand(intent, flags, startId)

    }

    private fun stopRecorder() {

        try {
            recorder.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        recorder.reset()
        recorder.release()
    }

    private fun recordStuff() {

        try {
            recorder.prepare()
            recorder.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}