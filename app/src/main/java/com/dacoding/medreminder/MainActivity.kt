package com.dacoding.medreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dacoding.medreminder.presentation.ui.theme.MedReminderTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val dateDialogState = rememberMaterialDialogState()
            val timeDialogState = rememberMaterialDialogState()

            var pickedDate by remember {
                mutableStateOf(LocalDate.now())
            }
            var pickedTime by remember {
                mutableStateOf(LocalTime.NOON)
            }

            MedReminderTheme {
                val context = LocalContext.current
                val service = NotificationService(context, viewModel)
                var hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED,
                        )
                    } else {
                        mutableStateOf(true)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.SCHEDULE_EXACT_ALARM
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else {
                        mutableStateOf(true)
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            hasNotificationPermission = isGranted
                        }
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var nameState by remember { mutableStateOf("") }
                        var dosageState by remember { mutableStateOf("") }

                        TextField(
                            value = nameState,
                            onValueChange = {
                                nameState = it
                            },
                            label = { Text(text = "Enter the name of the medication") },
                            placeholder = { Text(text = "Name") }
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        TextField(
                            value = dosageState,
                            onValueChange = {
                                dosageState = it
                            },
                            label = { Text(text = "Enter the dosage") },
                            placeholder = { Text(text = "Dosage") },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.NumberPassword
                            )


                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { dateDialogState.show() }) {
                            Text(text = "Select a date")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = pickedDate.toString())
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = { timeDialogState.show() }) {
                            Text(text = "Select a time")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = pickedTime.toString())
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                permissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }

                            if (hasNotificationPermission) {
                                viewModel.onEvent(
                                    MainEvent.Confirm(
                                        name = nameState,
                                        dosage = dosageState.toInt(),
                                        date = pickedDate,
                                        time = pickedTime
                                    )
                                )
                                service.scheduleNotification()
                                Toast.makeText(context, "Confirmed", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Text(text = "Confirm")
                        }
                    }
                }
                MaterialDialog(
                    dialogState = dateDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }
                ) {
                    datepicker(
                        initialDate = LocalDate.now(),
                        title = "Pick a date"
                    ) {
                        pickedDate = it
                    }
                }

                MaterialDialog(
                    dialogState = timeDialogState,
                    buttons = {
                        positiveButton(text = "Ok")
                        negativeButton(text = "Cancel")
                    }
                ) {
                    timepicker(
                        initialTime = LocalTime.NOON,
                        title = "Pick a date"
                    ) {
                        pickedTime = it
                    }
                }
            }
        }
    }
}

