package com.hamidreza.moderntodo.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.hamidreza.moderntodo.utils.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context:Context) {

    private val dataStore = context.createDataStore("user_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {
        val sortOrder = SortOrder.valueOf(
            it[PreferencesKeys.SORT_ORDER] ?: SortOrder.BY_NAME.toString()
        )
        val hideCompleted = it[PreferencesKeys.HIDE_COMPLETED] ?: false
        FilterPreferences(sortOrder,hideCompleted)
    }

    suspend fun updateSortOrder(sortOrder: SortOrder){
        dataStore.edit {
            it[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean){
        dataStore.edit {
            it[PreferencesKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKeys{
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }
    data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)
}