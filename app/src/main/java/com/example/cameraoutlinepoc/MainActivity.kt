package com.example.cameraoutlinepoc

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.annotation.RequiresApi
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    private lateinit var camera: Camera
    private var cameraPreview: CameraPreview? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /** Check if this device has a camera */

        findViewById<Button>(R.id.open_cam).setOnClickListener {
            _init()
        }

        requestPermissions(arrayOf(android.Manifest.permission.CAMERA), 123)

    }

    private fun _init() {



        if(checkCameraHardware()){
            camera = getCameraInstance()!!
            camera.setDisplayOrientation(90)

            cameraPreview = camera?.let {
                CameraPreview(this, it)
            }

            cameraPreview?.also {
                val preview: FrameLayout = findViewById(R.id.camera_preview)
                preview.addView(it)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        _init()
    }

    private fun checkCameraHardware(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        } else {
            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
        }
    }


    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }
}