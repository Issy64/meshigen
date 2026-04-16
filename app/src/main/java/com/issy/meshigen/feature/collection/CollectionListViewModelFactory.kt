package com.issy.meshigen.feature.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.issy.meshigen.data.repository.CollectionRepository

class CollectionListViewModelFactory(
    private val repository: CollectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectionListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}