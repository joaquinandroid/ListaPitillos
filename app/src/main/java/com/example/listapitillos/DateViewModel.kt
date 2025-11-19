package com.example.listapitillos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class DateViewModel(private val manager: DataStoreManager) : ViewModel(){

    val datesState: StateFlow<List<Date>> = manager.datesFlow
        .stateIn(
            scope = viewModelScope,
            // Empieza a recopilar tan pronto como haya observadores.
            started = SharingStarted.Companion.WhileSubscribed(5000),
            // Valor inicial mientras se carga el DataStore.
            initialValue = emptyList()
        )

    fun saveNextDate() {
        viewModelScope.launch {
            val currentDates = manager.datesFlow.first()

            val indexToSave: Int

            if (currentDates.size < MAX_DATES) {
                indexToSave = currentDates.size + 1
            } else {
                indexToSave = 1
            }

            manager.saveDateAtIndex(indexToSave, Date())

        }
    }

}