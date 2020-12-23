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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")

    val preferenceFlow = preferencesManager.preferencesFlow


    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = combine(
        searchQuery, preferenceFlow
    ) { searchQuery, preferenceFlow ->
        Orders(searchQuery, preferenceFlow.sortOrder, preferenceFlow.hideCompleted)
    }.flatMapLatest {
        dao.getTasks(it.searchQuery, it.sortOrder, it.hideCompleted)
    }

    val getTasks = tasksFlow.asLiveData()

    fun onSortOrderSelecte(sortOrder: SortOrder) =
        viewModelScope.launch {
            preferencesManager.updateSortOrder(sortOrder)
        }

     fun onHideCompletedClick(hideCompleted: Boolean) =
        viewModelScope.launch {
            preferencesManager.updateHideCompleted(hideCompleted)
        }



    fun onItemClick(task: Task){

    }
    fun onCheckBoxClick(task: Task, isChecked: Boolean) = viewModelScope.launch {
        dao.updateTask(task.copy(completed = isChecked))
    }

    fun saveTask(task: Task) = viewModelScope.launch {
        dao.saveTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        dao.deleteTask(task)
        tasksEventChannel.send(TasksEvent.ShowUndoDeleteTaskMessage(task))
    }

    sealed class TasksEvent{
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
    }

    data class Orders(val searchQuery: String, val sortOrder: SortOrder, val hideCompleted: Boolean)
}