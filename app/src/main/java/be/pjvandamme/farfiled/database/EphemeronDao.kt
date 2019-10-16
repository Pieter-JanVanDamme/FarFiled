package be.pjvandamme.farfiled.database

import androidx.lifecycle.LiveData
import androidx.room.*
import be.pjvandamme.farfiled.domain.Ephemeron

@Dao
interface EphemeronDao{
    @Insert
    fun insert(ephemeron: Ephemeron)

    @Update
    fun update(ephemeron: Ephemeron)

    // TODO: add function to retrieve last 3 Ephemera edited

    @Query("SELECT * FROM ephemeron WHERE relationId = :id")
    fun getAllEphemeraForRelation(id: Long): LiveData<List<Ephemeron>>

    @Delete
    fun delete(ephemeron: Ephemeron)
}