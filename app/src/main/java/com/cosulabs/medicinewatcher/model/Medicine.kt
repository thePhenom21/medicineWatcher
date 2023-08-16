package com.cosulabs.medicinewatcher.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity
data class Medicine(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "amount") val amount: String,
        @ColumnInfo(name = "time") val time: LocalTime,

){
        @Ignore var alarmSet: MutableState<Int> = mutableStateOf(0)

        val createdDateFormatted : String
                @RequiresApi(Build.VERSION_CODES.O)
                get() = time.format(DateTimeFormatter.ofPattern("HH:mm"))
}