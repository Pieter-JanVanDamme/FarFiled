package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import be.pjvandamme.farfiled.model.Face
import be.pjvandamme.farfiled.model.Relation
import be.pjvandamme.farfiled.persistence.FarFiledDatabase.Companion.getInstance
import be.pjvandamme.farfiled.persistence.repository.FaceRepository
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import be.pjvandamme.farfiled.util.UI_FACES_POPULATE_AMOUNT
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber

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


    private var _showCouldNotPopulateRelationsListSnackbar = MutableLiveData<Boolean>()
    val showCouldNotPopulateRelationsListSnackbar: LiveData<Boolean>
        get() = _showCouldNotPopulateRelationsListSnackbar

    private var _showGenerateRelationsListButton = MutableLiveData<Boolean>()
    val showGenerateRelationsListButton: LiveData<Boolean>
        get() = _showGenerateRelationsListButton


    private val _navigateToRelationDetail = MutableLiveData<Long>()
    val navigateToRelationDetail
        get() = _navigateToRelationDetail

    fun addFacesToRelationsList(amount: Int){
        if(!faces.value.isNullOrEmpty()) {
            Timber.i("Size of faces is ${faces.value?.size}")
            var facesList: List<Face>? = faces.value?.take(amount)
            facesList?.forEach {
                Timber.i("About to execute relationFromFace for ${it.name}")
                relationFromFace(it)
            }
            if(faces.value.isNullOrEmpty())
                _showGenerateRelationsListButton.value = false
        }
        else
            _showCouldNotPopulateRelationsListSnackbar.value = true
    }

    private fun relationFromFace(newFace: Face){
        Timber.i("relationFromFace executing, about to launch insert for ${newFace.name}")
        uiScope.launch {
            relationRepository.insert(
                Relation(
                    name = newFace.name,
                    synopsis = newFace.position,
                    avatarUrl = newFace.photo
                )
            )
            faceRepository.delete(newFace)
        }
    }

    fun showGenerateRandomRelationsButton(){
        _showGenerateRelationsListButton.value = true
    }

    fun hideGenerateRandomRelationsButton(){
        _showGenerateRelationsListButton.value = false
    }

    fun showCouldNotPopulateSnackbar(){
        _showCouldNotPopulateRelationsListSnackbar.value = true
    }

    fun doneShowingCouldNotPopulateSnackbar(){
        _showCouldNotPopulateRelationsListSnackbar.value = false
    }

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