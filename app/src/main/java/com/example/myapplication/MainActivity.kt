package com.example.myapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme() {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }

}

@Composable
fun Screen(modifier: Modifier){
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        isGranted : Boolean ->
        if (isGranted){
            readFirstContact(context)
        }
        else{
            Toast.makeText(context, "Contact Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Row(
        modifier = modifier.fillMaxWidth().padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ){
        Button(
        onClick = {
            val intent = Intent(context, SimpleService::class.java)
            context.startService(intent)
        }
        ) {
            Text("Start Service")
        }
        Button(
        onClick = {
            val intent = Intent(context, SimpleService::class.java)
            context.stopService(intent)
        }
        ) {
            Text("Stop Service")
        }
        Button(
        onClick = {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission){
                readFirstContact(context)
            }else{
                permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
            }
        }
        ) {
        Text("Read Contact")
        }
    }

}

private fun readFirstContact(context: Context){
    val cursor = context.contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
        null,
        null,
        null
    )

    cursor?.use {
        if(it.moveToFirst()){
            val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            val contactName = if (nameIndex != -1) it.getString(nameIndex) else "Unknown"
            Toast.makeText(context, "Contact: $contactName", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context, "No contacts found on device", Toast.LENGTH_SHORT).show()
        }
    }
}