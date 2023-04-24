package com.oceantech.monitorsaude.database.dao

import androidx.room.*
import com.oceantech.monitorsaude.model.Medicamento

@Dao
interface MedicamentoDao {

    @Query("SELECT * FROM Medicamento")
    suspend fun getAll(): List<Medicamento>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicamento: Medicamento)

    @Query("SELECT * FROM Medicamento WHERE id = :id")
    suspend fun getById(id: Int): Medicamento?

    @Delete
    suspend fun delete(medication: Medicamento)

}

