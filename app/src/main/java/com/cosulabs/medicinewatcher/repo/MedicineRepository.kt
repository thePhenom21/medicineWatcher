package com.cosulabs.medicinewatcher.repo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cosulabs.medicinewatcher.converters.Converter
import com.cosulabs.medicinewatcher.dao.MedicineDao
import com.cosulabs.medicinewatcher.model.Medicine

@Database(entities = [Medicine::class], version = 1)
@TypeConverters(Converter::class)
abstract class MedicineRepository : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao

    companion object {
        @Volatile
        private var Instance: MedicineRepository? = null

        fun getDatabase(context: Context): MedicineRepository {
            // if the Instance is not null, return it, otherwise create a new database instance.
            if (Instance == null ){
                return Room.databaseBuilder(context, MedicineRepository::class.java, "medicine")
                    .allowMainThreadQueries()
                    .build()
                    .also { Instance = it }
            }
            return Instance!!
        }
    }

}