package kushal.application.callrecoder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast

class CallReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context?, intent: Intent?) {

        var isCalling = false
        val pref = context!!.getSharedPreferences("sharedPref", Context.MODE_PRIVATE)

        val service = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        service.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)

                if (TelephonyManager.CALL_STATE_RINGING == state) {

                    val i = Intent(context, DialogAct::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    i.putExtra("number",intent!!.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER))
                    context.startActivity(i)
                }

                if (state == TelephonyManager.CALL_STATE_OFFHOOK) {

                    isCalling = true

                    if (pref.getBoolean("record", false))
                        Toast.makeText(context, "started recorder", Toast.LENGTH_SHORT).show()

                }

                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (isCalling) { // after coming back from call

                        isCalling = false

                        if (pref.getBoolean("record", false)) {

                            recordStuff()
                            Toast.makeText(context, "saved recording", Toast.LENGTH_SHORT).show()

                            pref.edit().putBoolean("record", false).apply()
                        }

                        val i = Intent(context, ClearStack::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        i.flags =
                            Intent.FLAG_ACTIVITY_NO_ANIMATION or Intent.FLAG_ACTIVITY_NO_HISTORY
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(i)

                    }
                }

            }
        }, PhoneStateListener.LISTEN_CALL_STATE)


    }

    private fun recordStuff() {


    }
}