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
    var recorder: MediaRecorder? = null
    val context = this
    var is_recording = false

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
                    (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))

                val date = Date()
                val sdf = "dd-MM-yyyy--hh:mm:ss"
                val format = DateFormat.format(sdf, date.time)

                recorder = MediaRecorder()
                recorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
                recorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                recorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

                recorder?.setOutputFile("${file.absoluteFile}/${format} recorder.mp3")

                if (TelephonyManager.CALL_STATE_RINGING == state) {

                    val i = Intent(context, DialogAct::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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

                        onDestroy()
                    }

                    isCalling = false

                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE)

        return START_STICKY

    }

    private fun stopRecorder() {

        try {
            recorder!!.stop()
            is_recording = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        recorder?.reset()
        recorder?.release()
        recorder = null

    }

    private fun recordStuff() {

        try {
            if (recorder == null)
                recorder = MediaRecorder()

            if (!is_recording) {
                recorder?.prepare()
                recorder?.start()
                is_recording = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        stopSelf()
    }

}