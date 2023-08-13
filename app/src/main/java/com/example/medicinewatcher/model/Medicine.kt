package com.example.medicinewatcher.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity
data class Medicine(
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "amount") val amount: String,
        @ColumnInfo(name = "time") val time: LocalTime
){
        val createdDateFormatted : String
                @RequiresApi(Build.VERSION_CODES.O)
                get() = time.format(DateTimeFormatter.ofPattern("HH:mm"))
}