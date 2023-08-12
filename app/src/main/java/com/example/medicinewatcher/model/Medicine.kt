package com.example.medicinewatcher.model

import androidx.room.*

@Entity
data class Medicine(
        @PrimaryKey val id : Int,
        @ColumnInfo(name = "name") val name : String,
        @ColumnInfo(name = "amount") val amount : String,
        @ColumnInfo(name = "time") val time : String
)