package com.dcerna.proyectofinal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Nota::class, Recordatorio::class, Multimedia::class],
    version = 3,
    exportSchema = false
)
abstract class NotasBD : RoomDatabase() {
    abstract fun DAONotas(): DAONotas

    companion object {
        private var INSTANCE: NotasBD? = null
        fun getInstance(context: Context): NotasBD {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotasBD::class.java,
                        "notas_db"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}