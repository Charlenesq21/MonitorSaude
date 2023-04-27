package com.oceantech.monitorsaude

import android.app.Application
import com.oceantech.monitorsaude.database.AppDatabase.Companion.getInstance
import com.oceantech.monitorsaude.database.dao.MedicamentoDao


class MyApplication : Application() {
    private var medicamentoDao: MedicamentoDao? = null
    private var medicamentoDataSource: MedicamentoDataSource? = null
    var medicamentoRepository: MedicamentoRepository? = null

    override fun onCreate() {
        super.onCreate()
        medicamentoDao = getInstance(this).medicamentoDao()
        medicamentoDataSource = MedicamentoDataSource(medicamentoDao!!)
        medicamentoRepository = MedicamentoRepository(medicamentoDataSource!!)
    }
}