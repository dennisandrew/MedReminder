package com.dacoding.medreminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dacoding.medreminder.ui.theme.MedReminderTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedReminderTheme {
                val context = LocalContext.current
                val service = NotificationService(context)
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
                        Button(onClick = {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                permissionLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }) {
                            Text(text = "Request permission")
                        }
                        Button(onClick = {
                            if (hasNotificationPermission) {
                                service.showNotification()
                            }

                        }) {
                            Text(text = "Show notification")
                        }
                    }
                }
            }
        }
    }
}

