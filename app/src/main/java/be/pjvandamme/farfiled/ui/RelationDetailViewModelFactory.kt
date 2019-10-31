package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.pjvandamme.farfiled.persistence.repository.RelationLifeAreaRepository
import be.pjvandamme.farfiled.persistence.repository.RelationRepository
import java.lang.IllegalArgumentException

class RelationDetailViewModelFactory(
    private val relationKey: Long?,
    private val relationDataSource: RelationRepository,
    private val relationLifeAreaDataSource: RelationLifeAreaRepository,
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