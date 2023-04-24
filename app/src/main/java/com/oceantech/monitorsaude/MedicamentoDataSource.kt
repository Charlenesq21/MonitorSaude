package com.oceantech.monitorsaude

import com.oceantech.monitorsaude.database.dao.MedicamentoDao
import com.oceantech.monitorsaude.model.Medicamento

class MedicamentoDataSource(private val medicationDao: MedicamentoDao) {
    suspend fun getAll(): List<Medicamento> = medicationDao.getAll()

    suspend fun getById(id: Int): Medicamento? = medicationDao.getById(id)

    suspend fun insert(medication: Medicamento) = medicationDao.insert(medication)

    suspend fun delete(medication: Medicamento) = medicationDao.delete(medication)
}
