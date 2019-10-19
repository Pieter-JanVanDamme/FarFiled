package be.pjvandamme.farfiled.presentation.relationslist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import be.pjvandamme.farfiled.database.RelationDao
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}