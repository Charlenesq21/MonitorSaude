package com.oceantech.monitorsaude.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.oceantech.monitorsaude.database.converter.DateConverter
import com.oceantech.monitorsaude.database.converter.TimeListConverter
import com.oceantech.monitorsaude.database.dao.MedicamentoDao
import com.oceantech.monitorsaude.model.Medicamento

@Database(entities = [Medicamento::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, TimeListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicamentoDao(): MedicamentoDao

    companion object {

        private const val DATABASE_NAME = "medicamento"

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                this.instance = instance
                instance
            }
        }
    }
}
