package be.pjvandamme.farfiled.viewmodels

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*
import be.pjvandamme.farfiled.database.RelationDao
import be.pjvandamme.farfiled.domain.Relation
import timber.log.Timber

class CreateEditRelationViewModel (
    private val relationKey: Long?,
    val database: RelationDao,
    application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var relation = MutableLiveData<Relation?>()

    private var _navigateToRelationDetail = MutableLiveData<Boolean>()
    val navigateToRelationDetail: LiveData<Boolean>
        get() = _navigateToRelationDetail

    private var _navigateToRelationsList = MutableLiveData<Boolean>()
    val navigateToRelationsList: LiveData<Boolean>
        get() = _navigateToRelationsList

    init {
        if(relationKey != null)
            initializeRelation(relationKey)
    }

    private fun initializeRelation(relationId: Long){
        uiScope.launch{
            relation.value = getRelationFromDatabase(relationId)
        }
    }

    private suspend fun getRelationFromDatabase(relationId: Long): Relation?{
        return withContext(Dispatchers.IO){
            var relation = database.get(relationId)
            relation
        }
    }

    fun onSave(name: String, synopsis: String){
        // TODO: disable save if nothing has been changed
        Timber.i("Got name: " + name + " and synopsis: " + synopsis)
        if(relationKey == null){
            uiScope.launch{
                relation.value = Relation(0, name, synopsis, false)
                insert(relation.value!!)
            }
        }
        else{
            uiScope.launch{
                relation.value?.name = name
                relation.value?.synopsis = synopsis
                update(relation.value)
            }
        }
        // TODO: if CREATEEDITRELATIONFRAGMENT is both for viewing and editing, then this will
        // not be necessary upon simple saving
        _navigateToRelationsList.value = true
    }

    private suspend fun insert(newRelation: Relation){
        withContext(Dispatchers.IO){
            database.insert(newRelation)
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
        _navigateToRelationDetail.value = false
        _navigateToRelationsList.value = false
    }

    fun onCancel(){
        if(relation != null){
            _navigateToRelationDetail.value = true
        }
        else
            _navigateToRelationsList.value = true
    }

    override fun onCleared(){
        viewModelJob.cancel()
    }
}