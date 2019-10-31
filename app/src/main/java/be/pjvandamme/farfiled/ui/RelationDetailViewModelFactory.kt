package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.pjvandamme.farfiled.dao.RelationDao
import be.pjvandamme.farfiled.dao.RelationLifeAreaDao
import java.lang.IllegalArgumentException

class RelationDetailViewModelFactory(
    private val relationKey: Long?,
    private val relationDataSource: RelationDao,
    private val relationLifeAreaDataSource: RelationLifeAreaDao,
    private val application: Application
): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T{
        if(modelClass.isAssignableFrom(RelationDetailViewModel::class.java)){
            return RelationDetailViewModel(
                relationKey,
                relationDataSource,
                relationLifeAreaDataSource,
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}