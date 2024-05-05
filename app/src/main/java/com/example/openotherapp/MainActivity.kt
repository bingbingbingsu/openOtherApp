package com.example.openotherapp

import android.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.openotherapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var button:Button
    private lateinit var button2:Button
    private lateinit var binding: ActivityMainBinding

    private var query = "com.android.chrome"
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        getApp()
        button = binding.button
        button.setOnClickListener {
            dispatchTakePictureIntent()
        }
        button2 = binding.button2
        button2.setOnClickListener() {
            val intent = packageManager.getLaunchIntentForPackage(query)
            startActivity(intent)
        }
        setContentView(binding.root)
    }
    fun createAlarm(message: String, hour: Int, minutes: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_HOUR, hour)
            putExtra(AlarmClock.EXTRA_MINUTES, minutes)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
    fun startTimer(message: String, seconds: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, message)
            putExtra(AlarmClock.EXTRA_LENGTH, seconds)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // Display error state to the user.
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getApp() {
        val result : List<PackageInfo> = getPackageManager().getInstalledPackages(0)    //특별한 경우 제외하곤 거의 0
        val appData = arrayListOf<String>()
        if(result != null){
            for(info in result){
                val packageName = info.packageName
                val appName = info.applicationInfo.loadLabel(packageManager)
                val category = ApplicationInfo.getCategoryTitle(this, info.applicationInfo.category)
                Log.d("APPINFO","$packageName")
//                appData.add(packageName+"   ::"+appName+" <"+category+">\n")
                appData.add(packageName)
            }
        }
        setupSpinner(appData)
    }

    private fun setupSpinner(items: ArrayList<String>) {
        val spinner= binding.spinner

        // Create an adapter to specify the layout for each item and populate the spinner
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinner.adapter = adapter

        // Optionally, you can set a listener to respond to item selection
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Handle item selection here
                val selectedItem = parent.getItemAtPosition(position).toString()
                query = selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

}