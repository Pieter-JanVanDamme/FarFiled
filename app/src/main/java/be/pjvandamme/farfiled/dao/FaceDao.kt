package be.pjvandamme.farfiled.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.pjvandamme.farfiled.persistence.DatabaseFace

@Dao
interface FaceDao{
    @Query("select * from databaseface")
    fun getAll(): LiveData<List<DatabaseFace>>

    // if we get the same face (picture) with different attributes, replace it
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg faces: DatabaseFace)
}