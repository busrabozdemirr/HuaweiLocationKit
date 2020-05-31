package com.example.huaweilocationkit

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.huawei.hmf.tasks.Task
import com.huawei.hms.location.FusedLocationProviderClient
import com.huawei.hms.location.LocationServices

class MainActivity : AppCompatActivity() {

    lateinit var mFusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkPermissionDialog()
        showAlertDialog()
    }

    fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(this,R.style.AlertDialogStyle)
        alertDialog
                .setTitle("Location Permisson")
                .setMessage("Huawei Mobile Servis wants to access this device location! Okay?")
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    getLocation()

                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which -> })
                .show()
    }

    fun checkPermissionDialog() {
        if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) !=
                PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.ACCESS_FINE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) !== PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        "android.permission.ACCESS_BACKGROUND_LOCATION"
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        }
    }

    fun getLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val lastLocation: Task<Location>? = mFusedLocationProviderClient.lastLocation
        lastLocation?.let { safeAct ->
            safeAct.addOnSuccessListener { location ->
                if (location == null) {
                    Log.i("Last Location", "Last location is null!")
                    return@addOnSuccessListener

                }
                Log.i(
                        "Last Location", "Last Location is: " + location.longitude
                        + " Longitude " + " , " + location.latitude + " Latitude "
                )
                Toast.makeText(this, "Location is " + location.longitude + " Longitude"
                        + "\nMission completed! "
                        , Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                Log.i("TAG",
                        "onRequestPermissionsResult: apply LOCATION PERMISSION successful"
                )
            } else {
                Log.i("TAG",
                        "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed"
                )
            }
        }
    }
}
