package com.example.listapitillos

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_FORMAT = "HH:mm:ss, dd-MM-yyyy"
private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.GERMANY)

fun Date.toStringFormat(): String = dateFormatter.format(this)
fun String.toDate(): Date? = try { dateFormatter.parse(this) } catch (e: Exception) { null }

const val MAX_DATES = 10
private const val DATE_KEY_PREFIX = "saved_date_"
fun getDateKey(index: Int) = stringPreferencesKey("$DATE_KEY_PREFIX$index")

private val Context.dataStore by preferencesDataStore(name = "momentos_pitillos")

class DataStoreManager(private val context: Context){

    val datesFlow: Flow<List<Date>> = context.dataStore.data
        .map { preferences ->
            (1..MAX_DATES).mapNotNull { index ->
                val key = getDateKey(index)
                preferences[key]?.toDate()
            }
        }

    suspend fun saveDateAtIndex(index: Int, date: Date) {
        require(index in 1..MAX_DATES) { "El índice debe estar entre 1 y $MAX_DATES" }

        context.dataStore.edit { preferences ->
            val key = getDateKey(index)
            preferences[key] = date.toStringFormat()
        }
    }

    suspend fun deleteDateAtIndex(index: Int) {
        require(index in 1..MAX_DATES) { "El índice debe estar entre 1 y $MAX_DATES" }

        context.dataStore.edit { preferences ->
            val key = getDateKey(index)
            // Usamos remove(key) para eliminar el par clave-valor de las preferencias.
            preferences.remove(key)
        }
    }
}