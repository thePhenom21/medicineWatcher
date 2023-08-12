package com.example.medicinewatcher.repo

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medicinewatcher.dao.MedicineDao
import com.example.medicinewatcher.model.Medicine

@Database(entities = [Medicine::class], version = 1)
abstract class MedicineRepository : RoomDatabase(){
    abstract fun medicineDao() : MedicineDao
}