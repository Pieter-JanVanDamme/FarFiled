package be.pjvandamme.farfiled.database

import androidx.lifecycle.LiveData
import androidx.room.*
import be.pjvandamme.farfiled.domain.Relation

@Dao
interface RelationDao{
    @Insert
    fun insert(relation: Relation): Long

    @Update
    fun update(relation: Relation)

    @Query("SELECT * from relation WHERE relationId = :id")
    fun get(id: Long): Relation

    @Query("SELECT * FROM relation WHERE relationId = :id")
    fun getRelationWithId(id: Long): LiveData<Relation>

    @Query("SELECT * from relation ORDER BY inFocus DESC, relationId ASC")
    fun getAllRelations(): LiveData<List<Relation>>

    @Delete
    fun delete(relation: Relation)
}