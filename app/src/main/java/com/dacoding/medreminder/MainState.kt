package com.dacoding.medreminder

import java.time.LocalDate
import java.time.LocalTime

data class MainState(
    val name: String = "",
    val dosage: Int = 0,
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.NOON
)
