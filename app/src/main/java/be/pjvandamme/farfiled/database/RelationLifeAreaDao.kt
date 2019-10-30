package be.pjvandamme.farfiled.database

import androidx.lifecycle.LiveData
import androidx.room.*
import be.pjvandamme.farfiled.models.RelationLifeArea

@Dao
interface RelationLifeAreaDao {
    @Insert
    fun insert(relationLifeArea: RelationLifeArea): Long?

    @Update
    fun update(relationLifeArea: RelationLifeArea)

    @Query("SELECT * FROM relation_life_area WHERE relationId = :id")
    fun getAllRelationLifeAreasForRelation(id: Long): LiveData<List<RelationLifeArea>>

    @Delete
    fun delete(relationLifeArea: RelationLifeArea)
}