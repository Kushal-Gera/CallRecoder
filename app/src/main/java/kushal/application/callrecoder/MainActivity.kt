package kushal.application.callrecoder

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.isNotEmpty()) {
            //nothing here
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                //denied
                requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
            } else {
                if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show()
                    onResume()
                } else {
                    //set to never ask again
                    showDialog()
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(
            this,
            R.style.dialog
        )
            .setTitle("Permissions are not Granted")
            .setMessage("The app is no more functional without these permissions")
            .setPositiveButton("Grant Now") { dialogInterface: DialogInterface, i: Int ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", packageName, null)
                startActivity(intent)
            }
            .setNegativeButton("Return") { dialogInterface: DialogInterface, i: Int ->
                finish()
            }
            .setCancelable(false)
            .create().show()
    }

}
