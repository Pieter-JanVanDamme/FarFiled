package be.pjvandamme.farfiled.persistence.repository

import androidx.lifecycle.LiveData
import be.pjvandamme.farfiled.model.Relation
import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class RelationRepository(private val database: FarFiledDatabase){
    suspend fun get(key: Long): Relation?{
        return withContext(Dispatchers.IO){
            var relation = database.relationDao.get(key)
            relation
        }
    }

    suspend fun getRelationWithId(key: Long): LiveData<Relation> {
        return withContext(Dispatchers.IO){
            var relation = database.relationDao.getRelationWithId(key)
            relation
        }
    }

    fun getAllRelations(): LiveData<List<Relation>> {
        return database.relationDao.getAllRelations()
    }

    suspend fun insert(newRelation: Relation): Long?{
        Timber.i("About to insert new relation (${newRelation.name} into database.")
        return withContext(Dispatchers.IO){
            var relationId = database.relationDao.insert(newRelation)
            relationId
        }
    }

    suspend fun update(relation: Relation?){
        if(relation != null) {
            withContext(Dispatchers.IO) {
                database.relationDao.update(relation)
            }
        }
    }

    suspend fun delete(relation: Relation?){
        if(relation != null){
            withContext(Dispatchers.IO){
                database.relationDao.delete(relation)
            }
        }
    }
}