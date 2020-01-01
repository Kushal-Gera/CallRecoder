package kushal.application.callrecoder

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.dialog.*

class DialogAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        pref.edit().putBoolean("record", false).apply()


        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        dialog.yes1.setOnClickListener {
            pref.edit().putBoolean("record", true).apply()
            dialog.dismiss()
        }
        dialog.yes2.setOnClickListener {
            pref.edit().putBoolean("record", true).apply()
            dialog.dismiss()
        }
        dialog.no1.setOnClickListener {
            dialog.dismiss()
            this.finish()
        }
        dialog.no2.setOnClickListener {
            dialog.dismiss()
            this.finish()
        }

    }
}
