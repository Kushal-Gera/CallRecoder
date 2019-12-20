package kushal.application.callrecoder

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DialogAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        pref.edit().putBoolean("record", false).apply()

        AlertDialog.Builder(this, R.style.dialog)
            .setTitle("Call Recorder")
            .setMessage("Do you want to record this call ?")
            .setPositiveButton("yes") { dialogInterface: DialogInterface, i: Int ->
                pref.edit().putBoolean("record", true).apply()
                //do nothing
            }
            .setNegativeButton("no") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                this.finish()
            }
            .setCancelable(false)
            .create().show()

    }
}
