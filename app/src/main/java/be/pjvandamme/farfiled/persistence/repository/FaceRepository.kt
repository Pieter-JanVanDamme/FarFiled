package be.pjvandamme.farfiled.persistence.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import be.pjvandamme.farfiled.model.Face
import be.pjvandamme.farfiled.network.UIFacesApi
import be.pjvandamme.farfiled.network.asDatabaseModel
import be.pjvandamme.farfiled.persistence.FarFiledDatabase
import be.pjvandamme.farfiled.persistence.asDomainModel
import be.pjvandamme.farfiled.util.UI_FACES_LIMIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FaceRepository(private val database: FarFiledDatabase){
    val faces: LiveData<List<Face>> = Transformations.map(database.faceDao.getAll()){
        it.asDomainModel()
    }

    // no return to avoid it being used for getting faces
    suspend fun refreshFaces(){
        withContext(Dispatchers.IO){
            val newFaces = UIFacesApi.retrofitService.getRandomUIFaces(UI_FACES_LIMIT).await()
            database.faceDao.insertAll(*newFaces.asDatabaseModel())
        }
    }
}