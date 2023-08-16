package com.cosulabs.medicinewatcher.dao

import androidx.room.*
import com.cosulabs.medicinewatcher.model.Medicine

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): List<Medicine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicine(medicine: Medicine)

    @Delete
    fun deleteMedicineById(medicine: Medicine)

}