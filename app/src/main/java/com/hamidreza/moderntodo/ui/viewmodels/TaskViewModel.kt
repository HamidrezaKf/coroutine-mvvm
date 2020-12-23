package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hamidreza.moderntodo.data.PreferencesManager
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao
import com.hamidreza.moderntodo.utils.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferenceFlow = preferencesManager.preferencesFlow

    private val tasksFlow = combine(
        searchQuery, preferenceFlow
    ) { searchQuery, preferenceFlow ->
        Orders(searchQuery, preferenceFlow.sortOrder, preferenceFlow.hideCompleted)
    }.flatMapLatest {
        dao.getTasks(it.searchQuery, it.sortOrder, it.hideCompleted)
    }

    fun onSortOrderSelecte(sortOrder: SortOrder) =
        viewModelScope.launch {
            preferencesManager.updateSortOrder(sortOrder)
        }

    suspend fun onHideCompletedClick(hideCompleted: Boolean) =
        viewModelScope.launch {
            preferencesManager.updateHideCompleted(hideCompleted)
        }


    val getTasks = tasksFlow.asLiveData()

    fun saveTask(task: Task) = viewModelScope.launch {
        dao.saveTask(task)
    }

    data class Orders(val searchQuery: String, val sortOrder: SortOrder, val hideCompleted: Boolean)
}