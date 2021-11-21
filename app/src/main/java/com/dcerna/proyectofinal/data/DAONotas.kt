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

    @Query("SELECT * FROM notas WHERE idNota = :id")
    fun getNota(id:String): Nota

    @Query("SELECT * FROM notas WHERE idNota = :id")
    fun getLiveNota(id:String): LiveData<Nota>

    @Query("SELECT * FROM notas")
    fun getNotas(): List<Nota>

    @Query("DELETE FROM multimedias WHERE idNota=:id")
    fun deleteAllMultimedio(id: String)

    @Query("DELETE FROM recordatorios WHERE idNota=:id")
    fun deleteAllRecordatorios(id: String)

    @Query("SELECT * FROM recordatorios WHERE idNota = :id")
    fun getLiveRecordatoriosPorIDNota(id: String): LiveData<List<Recordatorio>>

    @Query("SELECT * FROM recordatorios WHERE idNota = :id")
    fun getRecordatoriosPorIDNota(id: String): List<Recordatorio>

    @Query("SELECT * FROM multimedias WHERE idNota = :id")
    fun getLiveMultimediasPorIDNota(id: String): LiveData<List<Multimedia>>

    @Query("SELECT * FROM multimedias WHERE idNota = :id")
    fun getMultimediasPorIDNota(id: String): List<Multimedia>

    @Query("SELECT * FROM multimedias WHERE idMultimedia = :id")
    fun getMultimediaPorIDMultimedia(id: String): Multimedia
}