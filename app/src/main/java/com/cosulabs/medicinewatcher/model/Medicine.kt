package com.cosulabs.medicinewatcher.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.*
import com.google.firebase.firestore.DocumentReference
import java.time.LocalTime
import java.time.format.DateTimeFormatter

data class Medicine(
        var id: DocumentReference?,
        val name: String,
        val amount: String,
        val time: LocalTime,
        val userId: String

){
        var alarmSet: MutableState<Int> = mutableStateOf(1)

        val createdDateFormatted : String
                @RequiresApi(Build.VERSION_CODES.O)
                get() = time.format(DateTimeFormatter.ofPattern("HH:mm"))
}