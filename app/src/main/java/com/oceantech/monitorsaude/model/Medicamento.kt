package com.oceantech.monitorsaude.model

import androidx.room.Entity
import androidx.room.PrimaryKey

    @Entity(tableName = "Medicamento")
    data class Medicamento(
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val nome: String,
        val dosagem: String,
        val horarios: List<String> = emptyList(),
        val datas: List<String> = emptyList()
    )