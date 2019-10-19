package be.pjvandamme.farfiled.presentation.relationdetail

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import be.pjvandamme.farfiled.database.RelationDao
import be.pjvandamme.farfiled.domain.Relation
import timber.log.Timber

class RelationDetailViewModel (
    private val relationKey: Long?,
    val database: RelationDao,
    application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val relation = MediatorLiveData<Relation?>()
    fun getRelation() = relation

    private var _enableSaveButton = MutableLiveData<Boolean>()
    val enableSaveButton: LiveData<Boolean>
        get() = _enableSaveButton

    private var _navigateToRelationsList = MutableLiveData<Boolean>()
    val navigateToRelationsList: LiveData<Boolean>
        get() = _navigateToRelationsList

    init {
        initializeRelation()
    }

    private fun initializeRelation(){
        if(relationKey == null || relationKey == -1L)
            initializeNewRelation()
        else
            relation.addSource(database.getRelationWithId(relationKey), relation::setValue)
    }

    private fun initializeNewRelation(){
        uiScope.launch{
            relation.addSource(
                database.getRelationWithId(
                        insert(Relation(0L,"","",false))!!),
                relation::setValue)
        }
    }

    fun onEditRelation(
        relationNameText: String,
        relationSynopsisText: String
    ){
        _enableSaveButton.value = !compareRelationAttributes(relationNameText, relationSynopsisText)
    }

    private fun compareRelationAttributes(
        relationNameText: String,
        relationSynopsisText: String
    ): Boolean {
        return(relationNameText == relation.value?.name
                && relationSynopsisText == relation.value?.synopsis)
    }

    fun onSave(name: String, synopsis: String){
        // TODO: validate input
        Timber.i("Got name: " + name + " and synopsis: " + synopsis)
        uiScope.launch{
            relation.value?.name = name
            relation.value?.synopsis = synopsis
            update(relation.value)
        }
        // TODO: this one should go away, need some sort of up button instead
        _navigateToRelationsList.value = true
    }

    private suspend fun insert(newRelation: Relation): Long?{
        return withContext(Dispatchers.IO){
            var relationId = database.insert(newRelation)
            relationId
        }
    }

    private suspend fun update(relation: Relation?){
        if(relation != null) {
            withContext(Dispatchers.IO) {
                database.update(relation)
            }
        }
    }

    private suspend fun delete(relation: Relation?){
        if(relation != null){
            withContext(Dispatchers.IO){
                database.delete(relation)
            }
        }
    }

    fun doneNavigating(){
        _navigateToRelationsList.value = false
    }

    fun onCancel(){
        if(relationKey == null || relationKey == -1L)
            uiScope.launch{
                delete(relation.value)
            }
        _navigateToRelationsList.value = true
    }

    override fun onCleared(){
        viewModelJob.cancel()
    }
}