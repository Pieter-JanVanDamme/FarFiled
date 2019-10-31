package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import be.pjvandamme.farfiled.persistence.FarFiledDatabase.Companion.getInstance
import be.pjvandamme.farfiled.persistence.repository.FaceRepository
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RelationsListViewModel(
    val relationRepository: RelationRepository,
    application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val database = getInstance(application)
    val faceRepository = FaceRepository(database)

    init{
        uiScope.launch{
            faceRepository.refreshFaces()
        }
    }

    val faces = faceRepository.faces
    val relations = relationRepository.getAllRelations()

    private val _navigateToRelationDetail = MutableLiveData<Long>()
    val navigateToRelationDetail
        get() = _navigateToRelationDetail

    fun onRelationClicked(id: Long){
        _navigateToRelationDetail.value = id
    }

    fun onRelationDetailNavigated(){
        _navigateToRelationDetail.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}