package com.test2.abc

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.Debug
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.huawei.facerecognition.FaceManager
import com.huawei.facerecognition.FaceManager.AuthenticationCallback
import com.huawei.facerecognition.HwFaceManagerFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {
    private lateinit var faceManager: FaceManager
    val cancellationSignal = CancellationSignal()

    val faceCallback = object : AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
            super.onAuthenticationError(errorCode, errString)
        }

        override fun onAuthenticationSucceeded(result: FaceManager.AuthenticationResult?) {
            super.onAuthenticationSucceeded(result)
            Log.d("MainActivity.kt", "Face auth success!")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
        }

        override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
            super.onAuthenticationHelp(helpCode, helpString)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        faceManager = HwFaceManagerFactory.getFaceManager(applicationContext)

    }

    fun doFaceAuth(view: View) {
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            faceManager?.let {
                if (it.isHardwareDetected && it.hasEnrolledTemplates()) {
                    it.authenticate(null, cancellationSignal, 0, faceCallback, null)
                }
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    100)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                authButton.performClick()
            }
        }
    }
}