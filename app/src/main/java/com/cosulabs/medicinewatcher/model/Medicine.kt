package com.cosulabs.medicinewatcher.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Medicine(
        val id: String?,
        val name: String,
        val amount: String,
        val time: LocalTime,
        val user: String

){
        var alarmSet: MutableState<Int> = mutableStateOf(0)

        val createdDateFormatted : String
                @RequiresApi(Build.VERSION_CODES.O)
                get() = time.format(DateTimeFormatter.ofPattern("HH:mm"))
}