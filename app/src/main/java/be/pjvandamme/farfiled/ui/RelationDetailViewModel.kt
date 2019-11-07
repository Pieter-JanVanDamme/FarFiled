package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*
import be.pjvandamme.farfiled.model.LifeArea
import be.pjvandamme.farfiled.model.Relation
import be.pjvandamme.farfiled.model.RelationLifeArea
import be.pjvandamme.farfiled.network.AdorableAvatarApi
import be.pjvandamme.farfiled.persistence.repository.RelationLifeAreaRepository
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import timber.log.Timber

enum class RelationDetailEditText(val lifeArea: LifeArea?) {
    NAME(null),
    SYNOPSIS(null),
    EPHEMERA(LifeArea.EPHEMERA),
    PERSONAL(LifeArea.PERSONAL),
    VOCATION(LifeArea.VOCATION),
    DOMESTIC(LifeArea.DOMESTIC),
    COMMUNITY(LifeArea.COMMUNITY),
    LEISURE(LifeArea.LEISURE)
}

class RelationDetailViewModel (
    private val relationKey: Long?,
    val relationRepository: RelationRepository,
    val relationLifeAreaRepository: RelationLifeAreaRepository,
    application: Application
): AndroidViewModel(application) {
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val relation = MediatorLiveData<Relation?>()
    fun getRelation() = relation

    private val relationEdited = MediatorLiveData<Relation?>()
    fun getRelationEdited() = relationEdited

    private val relationLifeAreas = MediatorLiveData<List<RelationLifeArea?>>()
    fun getRelationLifeAreas() = relationLifeAreas

    private val relationLifeAreasEdited = MediatorLiveData<List<RelationLifeArea?>>()
    fun getRelationLifeAreasEdited() = relationLifeAreasEdited

    private val _adorableAvatarString = MutableLiveData<String>()
    val adorableAvatarString: LiveData<String>
        get() = _adorableAvatarString

    private var _enableSaveButton = MutableLiveData<Boolean>()
    val enableSaveButton: LiveData<Boolean>
        get() = _enableSaveButton

    private var _enableDeleteButton = MutableLiveData<Boolean>()
    val enableDeleteButton: LiveData<Boolean>
        get() = _enableDeleteButton

    private var _showNameEmptySnackbar = MutableLiveData<Boolean>()
    val showNameEmptySnackbar: LiveData<Boolean>
        get() = _showNameEmptySnackbar

    private var _navigateToRelationsList = MutableLiveData<Boolean>()
    val navigateToRelationsList: LiveData<Boolean>
        get() = _navigateToRelationsList

    init {
        initializeRelation()
    }

    private fun initializeRelation(){
        if(relationKey == null || relationKey == -1L) {
            initializeNewRelation()
            getAdorableAvatarFacialFeatures()
        }
        else {
            uiScope.launch {
                retrieveAvatarUrl()
                relation.addSource(
                    relationRepository.getRelationWithId(relationKey),
                    relation::setValue
                )
                relationLifeAreas.addSource(
                    relationLifeAreaRepository.getAllRelationLifeAreasForRelation(relationKey),
                    relationLifeAreas::setValue
                )
                relationEdited.addSource(
                    relation,
                    relationEdited::setValue
                )
                relationLifeAreasEdited.addSource(
                    relationLifeAreas,
                    relationLifeAreasEdited::setValue
                )
            }
            _enableDeleteButton.value = true
        }
    }

    private fun initializeNewRelation(){
        _enableDeleteButton.value = false
        uiScope.launch{
            var relationId = relationRepository.insert(Relation(0L,"","",null))
            initializeLifeAreasForRelation(relationId)
            relation.addSource(
                relationRepository.getRelationWithId(
                        relationId!!),
                relation::setValue)
            relationLifeAreas.addSource(
                relationLifeAreaRepository.getAllRelationLifeAreasForRelation(
                    relationId),
                relationLifeAreas::setValue)
            relationEdited.addSource(
                relation,
                relationEdited::setValue
            )
            relationLifeAreasEdited.addSource(
                relationLifeAreas,
                relationLifeAreasEdited::setValue
            )
        }
    }

    private fun initializeLifeAreasForRelation(relationId: Long?){
        if(relationId != null){
            enumValues<LifeArea>().forEach {
                uiScope.launch{
                    var relationLifeArea = RelationLifeArea(0L,relationId,it,"")
                    relationLifeAreaRepository.insert(relationLifeArea)
                }
            }
        }
    }

    private fun retrieveAvatarUrl(){
        // Retrieve Relation object separately, because we cannot observe the relation MediatorLiveData
        // from inside the ViewModel... Thus we are never sure that we have a Relation object
        // available from which to retrieve the URL
        uiScope.launch{
            var rel = relationRepository.get(relationKey!!)
            var avatarUrl = rel?.avatarUrl
            // if the Relation has no avatar, that means there was a network issue upon creating
            // the Relation -- in that case, we'll give him one now.
            if (avatarUrl.isNullOrEmpty()){
                getAdorableAvatarFacialFeatures()
                _enableSaveButton.value = true
            }
            else
                _adorableAvatarString.value = rel?.avatarUrl
        }
    }

    private fun getAdorableAvatarFacialFeatures(){
        uiScope.launch{
            var getFeaturesDeferred = AdorableAvatarApi.retrofitService.getFacialFeatures()
            try{
                var result = getFeaturesDeferred.await()
                _adorableAvatarString.value = "https://api.adorable.io/avatars/face/" +
                        result.features.eyes.shuffled().take(1)[0] + "/" +
                        result.features.nose.shuffled().take(1)[0] + "/" +
                        result.features.mouth.shuffled().take(1)[0] + "/" +
                        result.features.COLOR_PALETTE.shuffled().take(1)[0]
                relation.value?.avatarUrl = _adorableAvatarString.value
            } catch(t:Throwable){
                _adorableAvatarString.value = null
            }
        }
    }

    // TODO: check why _enableSaveButton is incorrect 1) when removing what was edited, 2) when rotating the device
    fun onEditRelation(changedText: String, relationDetailEditText: RelationDetailEditText){
        Timber.i("Received \"" + changedText + "\" from " + relationDetailEditText.toString())
        _enableSaveButton.value = !compareRelationAttributes(changedText, relationDetailEditText)
        when(relationDetailEditText){
            RelationDetailEditText.NAME -> relationEdited.value?.name = changedText
            RelationDetailEditText.SYNOPSIS -> relationEdited.value?.synopsis = changedText
            else -> (relationLifeAreas.value?.singleOrNull{
                it?.lifeArea == relationDetailEditText.lifeArea})?.content = changedText
        }
    }

    private fun compareRelationAttributes(changedText: String,
                                          relationDetailEditText: RelationDetailEditText
    )
            : Boolean {
        when(relationDetailEditText){
            RelationDetailEditText.NAME -> return relation.value?.name == changedText
            RelationDetailEditText.SYNOPSIS -> return relation.value?.synopsis == changedText
            else -> return (relationLifeAreasEdited.value?.singleOrNull{
                it?.lifeArea == relationDetailEditText.lifeArea})?.content == changedText
        }
    }

    fun onSave(){
        if(!relationEdited.value?.name.isNullOrEmpty()) {
            uiScope.launch {
                relation.value?.name = relationEdited.value?.name ?: ""
                relation.value?.synopsis = relationEdited.value?.synopsis ?: ""
                relationRepository.update(relation.value)
                relationLifeAreas.value?.forEach {
                    it?.let {
                        it.content = (relationLifeAreasEdited.value?.singleOrNull{ copy ->
                            it.lifeArea == copy?.lifeArea})?.content ?: ""
                        relationLifeAreaRepository.update(it)
                    }
                }
            }
            _navigateToRelationsList.value = true
        }
        else
            _showNameEmptySnackbar.value = true
    }

    fun doneShowingNameEmptySnackbar(){
        _showNameEmptySnackbar.value = false
    }

    fun doneNavigating(){
        _navigateToRelationsList.value = false
    }

    fun onCancel(){
        if(relationKey == null || relationKey == -1L) {
            uiScope.launch {
                relationRepository.delete(relation.value)
            }
        }
        _navigateToRelationsList.value = true
    }

    fun onDelete(){
        if(relationKey != null || relationKey != -1L){
            uiScope.launch{
                relationRepository.delete(relation.value)
            }
        }
        _navigateToRelationsList.value = true
    }

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }
}