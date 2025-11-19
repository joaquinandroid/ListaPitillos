package com.example.listapitillos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(private val manager: DataStoreManager): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DateViewModel::class.java)) {
            // Aquí es donde se crea la instancia de tu ViewModel,
            // pasando la dependencia DataStoreManager.
            return DateViewModel(manager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}