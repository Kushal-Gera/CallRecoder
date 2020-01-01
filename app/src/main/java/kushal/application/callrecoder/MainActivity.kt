package kushal.application.callrecoder

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    var player: MediaPlayer? = null

    val perms = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.PROCESS_OUTGOING_CALLS,
        Manifest.permission.RECORD_AUDIO
    )

    private var list = arrayListOf<String>()
    private var listPaths = arrayListOf<File>()


    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, 101)
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED) {

                var root =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)

                lisDir(root)

                swipe.setOnRefreshListener {
                    swipe.isRefreshing = false
                    root =
                        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath)
                    lisDir(root)
                }

                listView.setOnItemClickListener { adapterView, view, pos, l ->
                    Toast.makeText(this, "Playing", Toast.LENGTH_SHORT).show()
                    playAudio(listPaths[pos].path)
                }


                stop.setOnClickListener {
                    if (player!!.isPlaying) {
                        player!!.stop()
                        player!!.reset()
                    }
                    stop.visibility = View.GONE
                }


//                val intent = Intent()
//                val packageName = this.packageName
//                val pm =
//                    this.getSystemService(Context.POWER_SERVICE) as PowerManager
//
//                if (!pm.isIgnoringBatteryOptimizations(packageName))
//                    intent.action = Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS
//                else {
//                    intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
//                    intent.data = Uri.parse("package:$packageName")
//                }
//                startActivity(intent)
            }
        }

    }

    private fun playAudio(path: String) {

        try {
            val file = File(path)
            val mimeType = "audio/*"
            val fileURI = FileProvider.getUriForFile(
                applicationContext,
                applicationContext
                    .packageName + ".provider", file
            )
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(fileURI, mimeType)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            Log.e(
                "TAG", "FAILED TO PLAY SONG $e"
            )
        }

        return

//
//
//        if (player == null) {
//            player = MediaPlayer()
//
//        }
//
//
//        try {
//            if (player!!.isPlaying) {
//                player!!.stop()
//                player!!.reset()
//            }
//        } catch (e: Exception) {
//            Toast.makeText(this, "Error Occurred by ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//
//        try {
//            player!!.setDataSource(path)
//            player!!.setVolume(80f, 80f)
//            player!!.prepareAsync()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "Error Occurred by ${e.message}", Toast.LENGTH_SHORT).show()
//        }
//
//        player!!.setOnPreparedListener {
//            player!!.start()
//            Toast.makeText(this, "playing", Toast.LENGTH_SHORT).show()
//            stop.visibility = View.VISIBLE
//        }
//
//        player?.setOnCompletionListener {
//            Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
//            player?.reset()
//            player?.release()
//            player = null
//        }

    }

    private fun lisDir(root: File) {

        val files = root.listFiles()
        list.clear()
        if (files == null) {
            return
        }
        for (file: File in files) {
            if (file.name.contains("recorder.mp3")) {
                list.add(file.name)
                listPaths.add(file)
            }
        }

        val directoryList = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = directoryList

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.isNotEmpty()) {
            //nothing here
        } else {
            if (shouldShowRequestPermissionRationale(perms[0])) {
                //denied
                requestPermissions(perms, 101)
            } else {
                if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_GRANTED) {
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

    override fun onPause() {
        player?.stop()
        super.onPause()
    }

    override fun onDestroy() {
        player?.stop()
        player?.reset()
        player?.release()

        player = null
        super.onDestroy()
    }


}
