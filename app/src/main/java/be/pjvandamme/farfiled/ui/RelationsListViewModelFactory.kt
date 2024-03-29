package be.pjvandamme.farfiled.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import be.pjvandamme.farfiled.persistence.repository.RelationRepository

class RelationsListViewModelFactory(
    private val dataSource: RelationRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RelationsListViewModel::class.java)) {
            return RelationsListViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}