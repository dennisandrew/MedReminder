package com.dacoding.medreminder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Confirm -> {
                state = MainState(
                    name = event.name,
                    dosage = event.dosage,
                    date = event.date,
                    time = event.time
                )
            }

        }
    }
}

