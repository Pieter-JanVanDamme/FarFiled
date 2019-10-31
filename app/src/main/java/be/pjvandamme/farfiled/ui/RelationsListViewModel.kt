package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import be.pjvandamme.farfiled.dao.RelationDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class RelationsListViewModel(
    val database: RelationDao,
    application: Application
): AndroidViewModel(application){
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val relations = database.getAllRelations()

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