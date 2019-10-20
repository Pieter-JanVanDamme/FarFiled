package be.pjvandamme.farfiled.presentation.relationdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import be.pjvandamme.farfiled.database.RelationDao
import be.pjvandamme.farfiled.database.RelationLifeAreaDao
import be.pjvandamme.farfiled.domain.LifeArea
import be.pjvandamme.farfiled.domain.Relation
import be.pjvandamme.farfiled.domain.RelationLifeArea
import be.pjvandamme.farfiled.network.AdorableAvatarApi

enum class AdorableAvatarStatus{
    LOADING, ERROR, DONE
}

class RelationDetailViewModel (
    private val relationKey: Long?,
    val relationDatabase: RelationDao,
    val relationLifeAreaDatabase: RelationLifeAreaDao,
    application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val relation = MediatorLiveData<Relation?>()
    fun getRelation() = relation

    private val relationLifeAreas = MediatorLiveData<List<RelationLifeArea?>>()
    fun getRelationLifeAreas() = relationLifeAreas

    private val _adorableAvatarString = MutableLiveData<String>()
    val adorableAvatarString: LiveData<String>
        get() = _adorableAvatarString

    private var _enableSaveButton = MutableLiveData<Boolean>()
    val enableSaveButton: LiveData<Boolean>
        get() = _enableSaveButton

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
            retrieveAvatarUrl()
            relation.addSource(
                relationDatabase.getRelationWithId(relationKey),
                relation::setValue)
            relationLifeAreas.addSource(
                relationLifeAreaDatabase.getAllRelationLifeAreasForRelation(relationKey),
                relationLifeAreas::setValue)
        }
    }

    private fun initializeNewRelation(){
        uiScope.launch{
            var relationId = insert(Relation(0L,"","",null,false))
            initializeLifeAreasForRelation(relationId)
            relation.addSource(
                relationDatabase.getRelationWithId(
                        relationId!!),
                relation::setValue)
            relationLifeAreas.addSource(
                relationLifeAreaDatabase.getAllRelationLifeAreasForRelation(
                    relationId!!),
                relationLifeAreas::setValue)
        }
    }

    private fun initializeLifeAreasForRelation(relationId: Long?){
        if(relationId != null){
            enumValues<LifeArea>().forEach {
                uiScope.launch{
                    var relationLifeArea = RelationLifeArea(0L,relationId,it,"")
                    insert(relationLifeArea)
                }
            }
        }
    }

    private fun retrieveAvatarUrl(){
        // Retrieve Relation object separately, because we cannot observe the relation MediatorLiveData
        // from inside the ViewModel... Thus we are never sure that we have a Relation object
        // available from which to retrieve the URL
        uiScope.launch{
            var rel = get(relationKey!!)
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
                // ToDo: what if this fails?? -> Try again later!!
                _adorableAvatarString.value = "Failure: " + t.message
            }
        }
    }

    fun onEditRelation(
        relationNameText: String,
        relationSynopsisText: String,
        lifeAreaNowText: String,
        lifeAreaSelfText: String,
        lifeAreaWorkText: String,
        lifeAreaHomeText: String,
        lifeAreaCircleText: String,
        lifeAreaFunText: String
    ){
        _enableSaveButton.value = !compareRelationAttributes(
            relationNameText,
            relationSynopsisText,
            lifeAreaNowText,
            lifeAreaSelfText,
            lifeAreaWorkText,
            lifeAreaHomeText,
            lifeAreaCircleText,
            lifeAreaFunText
        )
    }

    private fun compareRelationAttributes(
        relationNameText: String,
        relationSynopsisText: String,
        lifeAreaNowText: String,
        lifeAreaSelfText: String,
        lifeAreaWorkText: String,
        lifeAreaHomeText: String,
        lifeAreaCircleText: String,
        lifeAreaFunText: String
    ): Boolean {
        var attributesEqual = true
        relationLifeAreas.value?.forEach{
            it?.let {
                when (it.lifeArea) {
                    LifeArea.EPHEMERA -> if(it.content != lifeAreaNowText) attributesEqual = false
                    LifeArea.PERSONAL -> if(it.content != lifeAreaSelfText) attributesEqual = false
                    LifeArea.VOCATION -> if (it.content != lifeAreaWorkText) attributesEqual = false
                    LifeArea.DOMESTIC -> if(it.content != lifeAreaHomeText) attributesEqual = false
                    LifeArea.COMMUNITY -> if(it.content != lifeAreaCircleText) attributesEqual = false
                    LifeArea.LEISURE -> if(it.content != lifeAreaFunText) attributesEqual = false
                }
            }
        }
        if( relationNameText != relation.value?.name)
            attributesEqual = false
        if(relationSynopsisText != relation.value?.synopsis)
            attributesEqual = false
        return attributesEqual
    }

    fun onSave(
        name: String,
        synopsis: String,
        nowLA: String,
        selfLA: String,
        workLA: String,
        homeLA: String,
        circleLA: String,
        funLA: String
    ){
        if(!name.isNullOrEmpty()) {
            uiScope.launch {
                relation.value?.name = name
                relation.value?.synopsis = synopsis
                update(relation.value)
                relationLifeAreas.value?.forEach {
                    it?.let {
                        when (it.lifeArea) {
                            LifeArea.EPHEMERA -> it.content = nowLA
                            LifeArea.PERSONAL -> it.content = selfLA
                            LifeArea.VOCATION -> it.content = workLA
                            LifeArea.DOMESTIC -> it.content = homeLA
                            LifeArea.COMMUNITY -> it.content = circleLA
                            LifeArea.LEISURE -> it.content = funLA
                        }
                        update(it)
                    }
                }
            }
            // TODO: this one should go away, need some sort of up button instead
            _navigateToRelationsList.value = true
        }
        else
            _showNameEmptySnackbar.value = true
    }

    private suspend fun get(key: Long): Relation?{
        return withContext(Dispatchers.IO){
            var relation = relationDatabase.get(key)
            relation
        }
    }

    private suspend fun insert(newRelation: Relation): Long?{
        return withContext(Dispatchers.IO){
            var relationId = relationDatabase.insert(newRelation)
            relationId
        }
    }

    private suspend fun insert(newRelationLifeArea: RelationLifeArea): Long?{
        return withContext(Dispatchers.IO){
            var relationLifeAreaId = relationLifeAreaDatabase.insert(newRelationLifeArea)
            relationLifeAreaId
        }
    }

    private suspend fun update(relation: Relation?){
        if(relation != null) {
            withContext(Dispatchers.IO) {
                relationDatabase.update(relation)
            }
        }
    }

    private suspend fun update(relationLifeArea: RelationLifeArea?){
        relationLifeArea?.let{
            withContext(Dispatchers.IO){
                relationLifeAreaDatabase.update(it)
            }
        }
    }

    private suspend fun delete(relation: Relation?){
        if(relation != null){
            withContext(Dispatchers.IO){
                relationDatabase.delete(relation)
            }
        }
    }

    fun doneShowingNameEmptySnackbar(){
        _showNameEmptySnackbar.value = false
    }

    fun doneNavigating(){
        _navigateToRelationsList.value = false
    }

    fun onCancel(){
        // TODO: right now, relation isn't actually deleted...
        if(relationKey == null || relationKey == -1L) {
            uiScope.launch {
                delete(relation.value)
            }
        }
        _navigateToRelationsList.value = true
    }

    override fun onCleared(){
        super.onCleared()
        viewModelJob.cancel()
    }
}