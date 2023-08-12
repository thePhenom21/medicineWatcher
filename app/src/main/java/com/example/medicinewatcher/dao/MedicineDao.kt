package com.example.medicinewatcher.dao

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.*
import com.example.medicinewatcher.model.Medicine

@Dao
interface MedicineDao {
    @Query("SELECT * FROM medicine")
    fun getAll(): List<Medicine>

    @Insert
    fun insertMedicine(medicine: Medicine)

    @Query("DELETE FROM medicine WHERE id =:id ")
    fun deleteMedicineById(id : Int)

}