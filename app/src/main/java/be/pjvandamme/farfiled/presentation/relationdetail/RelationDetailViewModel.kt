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
                database
                    .getRelationWithId(
                        insert(
                            Relation(0L,"","",false))!!),
                relation::setValue)
        }
    }

    fun onSave(name: String, synopsis: String){
        // TODO: disable save if nothing has been changed
        // TODO: validate input
        Timber.i("Got name: " + name + " and synopsis: " + synopsis)
        uiScope.launch{
            relation.value?.name = name
            relation.value?.synopsis = synopsis
            update(relation.value)
        }
        // TODO: if CREATEEDITRELATIONFRAGMENT is both for viewing and editing, then this will
        // not be necessary upon simple saving
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

    fun doneNavigating(){
        _navigateToRelationsList.value = false
    }

    fun onCancel(){
        _navigateToRelationsList.value = true
    }

    override fun onCleared(){
        viewModelJob.cancel()
    }
}