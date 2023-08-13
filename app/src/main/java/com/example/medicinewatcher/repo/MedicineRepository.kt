package com.example.medicinewatcher.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.medicinewatcher.dao.MedicineDao
import com.example.medicinewatcher.model.Medicine

@Database(entities = [Medicine::class], version = 1)
abstract class MedicineRepository : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var Instance: MedicineRepository? = null

        fun getDatabase(context: Context): MedicineRepository {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, MedicineRepository::class.java, "medicine")
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
        }
    }


}