package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted ->
        if (granted) {
            openCamera()
        }
        else {
            Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme() {
                var batteryLevel by remember { mutableIntStateOf(getBatteryPercentage()) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    CameraScreen(
                        modifier = Modifier.padding(innerPadding),
                        batteryLevel = batteryLevel,
                        onOpenCamera = {
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                        onSendBattery = {
                            batteryLevel = getBatteryPercentage()
                            val intent = Intent(this, SecondActivity::class.java).apply {
                                putExtra("BATTERY_LEVEL", batteryLevel)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    private fun getBatteryPercentage(): Int {
        val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun previewCameraScreen(){
    MyApplicationTheme(){
//        CameraScreen { }
    }
}

@Composable
fun CameraScreen(modifier: Modifier, batteryLevel: Int, onOpenCamera: () -> Unit,  onSendBattery: () -> Unit){
    Column(
        modifier = modifier.fillMaxWidth().padding(8.dp),
    ){
        Text("Battery: $batteryLevel")

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onOpenCamera) {
            Text("Open Camera")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onSendBattery) {
            Text("Send")
        }
    }

}

