package be.pjvandamme.farfiled.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import be.pjvandamme.farfiled.persistence.DatabaseFace

@Dao
interface FaceDao{
    @Query("select * from databaseface")
    fun getAll(): LiveData<List<DatabaseFace>>

    // if we get the same face (picture) with different attributes, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg faces: DatabaseFace)

    @Delete
    fun delete(face: DatabaseFace)
}