package com.dacoding.medreminder

import java.time.LocalDate
import java.time.LocalTime

sealed class MainEvent {
    data class Confirm(val name: String, val dosage: Int, val date: LocalDate, val time: LocalTime) : MainEvent()
}
