package be.pjvandamme.farfiled.persistence.repository

import androidx.lifecycle.LiveData
import be.pjvandamme.farfiled.model.RelationLifeArea
import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RelationLifeAreaRepository(private val database: FarFiledDatabase){

    suspend fun insert(newRelationLifeArea: RelationLifeArea): Long?{
        return withContext(Dispatchers.IO){
            var relationLifeAreaId = database.relationLifeAreaDao.insert(newRelationLifeArea)
            relationLifeAreaId
        }
    }

    suspend fun getAllRelationLifeAreasForRelation(key: Long): LiveData<List<RelationLifeArea>>{
        return withContext(Dispatchers.IO){
            var relationLifeAreas = database.relationLifeAreaDao.getAllRelationLifeAreasForRelation(key)
            relationLifeAreas
        }
    }

    suspend fun update(relationLifeArea: RelationLifeArea?){
        relationLifeArea?.let{
            withContext(Dispatchers.IO){
                database.relationLifeAreaDao.update(it)
            }
        }
    }
}