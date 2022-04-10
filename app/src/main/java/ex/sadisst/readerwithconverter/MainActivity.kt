package ex.sadisst.readerwithconverter

import android.Manifest
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import ex.sadisst.readerwithconverter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mPrefs: SharedPreferences
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var permissionDocumentsReading: ActivityResultLauncher<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        permissionDocumentsReading = registerForActivityResult(RequestPermission()) {
                isGranted: Boolean ->
            if (isGranted){
                Log.d("ReaderWithConverter_Debug", "requestPermissionIfNotGranted -> Permission granted")
            } else {
                Log.d("ReaderWithConverter_Debug", "requestPermissionIfNotGranted -> Permission denied")
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                binding.tvDir.text = result.data?.dataString
                Log.d("ReaderWithConverter_Debug", "$result")
                selectDirectory(binding.root)
            }
        }
    }

    private fun checkIfPermissionsGranted(): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionIfNotGranted() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                Toast.makeText(this@MainActivity,
                    "Permission is denied. Please allow application to manage storage from settings",
                    Toast.LENGTH_LONG).show()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                permissionDocumentsReading?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> permissionDocumentsReading?.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    fun selectDirectory(view: View) {
        if (checkIfPermissionsGranted()) {
            val selectDir = Intent(this@MainActivity, ListStorageActivity::class.java)
                selectDir.putExtra("path", Environment.getExternalStorageDirectory().path)

            Log.d("ReaderWithConverter_Debug", "Selecting directory")

            launcher?.launch(selectDir)
        } else {
            requestPermissionIfNotGranted()
        }
    }
}