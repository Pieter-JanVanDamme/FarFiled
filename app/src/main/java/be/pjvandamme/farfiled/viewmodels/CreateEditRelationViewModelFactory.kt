package be.pjvandamme.farfiled.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.pjvandamme.farfiled.database.RelationDao
import java.lang.IllegalArgumentException

class CreateEditRelationViewModelFactory(
    private val relationKey: Long?,
    private val dataSource: RelationDao,
    private val application: Application
): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(CreateEditRelationViewModel::class.java)){
            return CreateEditRelationViewModel(relationKey, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}