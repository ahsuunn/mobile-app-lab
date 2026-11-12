package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Student Registration") },
                            )
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        HorizontalDivider()
                        StudentRegistration()
                    }
                }
            }
        }
    }
}

@Composable
fun StudentRegistration() {
    val context = LocalContext.current

    var nameInput by remember { mutableStateOf("") }
    var idInput by remember { mutableStateOf("") }
    var student by remember { mutableStateOf<Student?>(null) }
    var isNotificationChecked by remember { mutableStateOf(true) }
    var isTermChecked by remember { mutableStateOf(false) }
    val isValid by remember {
        derivedStateOf {
            val isNameValid = !nameInput.isBlank()
            val isIdValid = !idInput.isBlank()

            isNameValid && isIdValid && isTermChecked
        }
    }
    var isPreview by remember { mutableStateOf(false) }

    var selectedPlant by remember { mutableStateOf("Standard") }

    fun handlePreview(name: String, id: String, isTermChecked: Boolean) {
        if (name.isBlank()) {
            Toast.makeText(context, "Fill the Name field", Toast.LENGTH_SHORT).show()
        } else if (id.isBlank()) {
            Toast.makeText(context, "Fill the Student ID field", Toast.LENGTH_SHORT).show()
        } else if (!isTermChecked) {
            Toast.makeText(context, "You must agree to the terms.", Toast.LENGTH_SHORT).show()
        } else {
            student = Student(name, id)
            isPreview = true
        }
    }

    fun handleSubmit() {
        Toast.makeText(context, "Submitted", Toast.LENGTH_SHORT).show()

    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = idInput,
            onValueChange = { idInput = it },
            label = { Text("Student ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Select Plan",
            fontWeight = FontWeight.Bold
        )
        PlanInput(onPlanSelected = {
            selectedPlant = it
        })
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val statusText = if (isNotificationChecked) "YES" else "NO"
            Text(text = "Notifications: $statusText")
            Switch(
                checked = isNotificationChecked,
                onCheckedChange = { isNotificationChecked = it },
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTermChecked,
                onCheckedChange = { isTermChecked = it }
            )
            Text(text = "I agree to the terms.")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { handlePreview(nameInput, idInput, isTermChecked) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Preview")
            }

            Button(
                enabled = isValid,
                onClick = { handleSubmit() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Submit")
            }
        }
        HorizontalDivider()
        if (isPreview) {
            PreviewCard(
                name = student?.name,
                id = student?.id,
                plan = selectedPlant,
                isNotificationChecked = isNotificationChecked,
                isTermAgreed = isTermChecked
            )

        }

    }
}

@Composable
fun PlanInput(onPlanSelected: (String) -> Unit) {
    val options = listOf("Basic", "Standard", "Expert")

    var selectedOption by remember { mutableStateOf(options[1]) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        for (option in options) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = option == selectedOption,
                    onClick = {
                        selectedOption = option
                        onPlanSelected(option)
                    },
                )
                Text(text = option)
            }
        }
    }
}

@Composable
fun PreviewCard(
    name: String?,
    id: String?,
    plan: String,
    isNotificationChecked: Boolean,
    isTermAgreed: Boolean
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "Name: $name")
            Text(text = "ID: $id")
            Text(text = "Plan: $plan")

            val notificationText = if (isNotificationChecked) "ON" else "OFF"
            Text(text = "Notifications: $notificationText")

            val termText = if (isTermAgreed) "True" else "False"
            Text(text = "Agreed: $termText")
        }
    }
}