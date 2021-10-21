package com.dcerna.proyectofinal.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAONotas {
    @Insert
    fun save(nota: Nota)

    @Insert
    fun save(vararg recordatorio: Recordatorio)

    @Insert
    fun save(vararg multimedia: Multimedia)

    @Update
    fun update(nota: Nota)

    @Update
    fun update(recordatorio: Recordatorio)

    @Update
    fun update(multimedia: Multimedia)

    @Delete
    fun delete(nota: Nota)

    @Delete
    fun delete(vararg recordatorio: Recordatorio)

    @Delete
    fun delete(vararg multimedia: Multimedia)

    @Query("SELECT * FROM notas")
    fun getLiveNotas(): LiveData<List<Nota>>

    @Query("SELECT * FROM notas")
    fun getNotas(): List<Nota>

    @Query("SELECT * FROM recordatorios WHERE idNota = :id")
    fun getLiveRecordatoriosPorIDNota(id: String): LiveData<List<Recordatorio>>

    @Query("SELECT * FROM recordatorios WHERE idNota = :id")
    fun getRecordatoriosPorIDNota(id: String): List<Recordatorio>

    @Query("SELECT * FROM multimedias WHERE idNota = :id")
    fun getLiveMultimediasPorIDNota(id: String): LiveData<List<Multimedia>>

    @Query("SELECT * FROM multimedias WHERE idNota = :id")
    fun getMultimediasPorIDNota(id: String): List<Multimedia>
}