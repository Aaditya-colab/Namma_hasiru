package com.example.nammahasiru

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.TimeUnit

class AddPlantActivity : AppCompatActivity() {

    private lateinit var uri: Uri
    private lateinit var db: AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    private val camLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                findViewById<ImageView>(R.id.img).setImageURI(uri)
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
            val locationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            val notificationGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissions[Manifest.permission.POST_NOTIFICATIONS] ?: false
            } else {
                true
            }
            
            if (!cameraGranted || !locationGranted || !notificationGranted) {
                Toast.makeText(this, "Permissions required for full functionality", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        db = AppDatabase.get(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkPermissions()

        findViewById<Button>(R.id.cam).setOnClickListener {
            openCamera()
        }

        findViewById<Button>(R.id.save).setOnClickListener {
            getLocationAndSave()
        }
    }

    private fun checkPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        
        if (permissions.any { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
            requestPermissionLauncher.launch(permissions.toTypedArray())
        }
    }

    private fun openCamera() {
        try {
            val file = File.createTempFile(
                "img_", ".jpg", getExternalFilesDir("Pictures")
            )
            uri = FileProvider.getUriForFile(
                this, "$packageName.provider", file
            )
            camLauncher.launch(uri)
        } catch (e: Exception) {
            Toast.makeText(this, "Error opening camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocationAndSave() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            savePlant()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lat = location.latitude
                lng = location.longitude
            }
            savePlant()
        }.addOnFailureListener {
            savePlant()
        }
    }

    private fun savePlant() {
        val species = findViewById<EditText>(R.id.species).text.toString()
        if (species.isBlank()) {
            Toast.makeText(this, "Please enter species name", Toast.LENGTH_SHORT).show()
            return
        }

        val plant = Plant(
            species = species,
            notes = findViewById<EditText>(R.id.notes).text.toString(),
            imageUri = if (::uri.isInitialized) uri.toString() else "",
            latitude = lat,
            longitude = lng,
            status = "Pending"
        )

        CoroutineScope(Dispatchers.IO).launch {
            db.dao().insert(plant)
            scheduleReminder(species)
        }

        finish()
    }

    private fun scheduleReminder(species: String) {
        val data = workDataOf("plantName" to species)
        
        // Temporarily set to 1 minute for testing. Change to 90 days for production.
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(1, TimeUnit.MINUTES) 
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}
