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
import timber.log.Timber

class FaceRepository(private val database: FarFiledDatabase){
    val faces: LiveData<List<Face>> = Transformations.map(database.faceDao.getAll()){
        it.asDomainModel()
    }

    suspend fun delete(face: Face?){
        if(face != null){
            withContext(Dispatchers.IO){
                database.faceDao.delete(face.asDatabaseModel())
            }
            Timber.i("FaceRepository.Delete(face: Face?) called for ${face.name}")
        }
    }
    // no return of Face objects to avoid it being used for getting faces
    suspend fun refreshFaces(){
        withContext(Dispatchers.IO){
            try {
                var newFaces = UIFacesApi.retrofitService.getRandomUIFaces(UI_FACES_LIMIT).await()
                database.faceDao.insertAll(*newFaces.asDatabaseModel())
                Timber.i("FaceRepository: faces refreshed.")
            } catch(t: Throwable){
                Timber.i("Could not refresh faces: $t")
            }
        }
    }
}