package com.oceantech.monitorsaude

import com.oceantech.monitorsaude.model.Medicamento

class MedicamentoRepository(private val dataSource: MedicamentoDataSource) {
    suspend fun getAll(): List<Medicamento> = dataSource.getAll()

    suspend fun getById(id: Int): Medicamento? = dataSource.getById(id)

    suspend fun insert(medicamento: Medicamento) = dataSource.insert(medicamento)

    suspend fun delete(medicamento: Medicamento) = dataSource.delete(medicamento)
}
