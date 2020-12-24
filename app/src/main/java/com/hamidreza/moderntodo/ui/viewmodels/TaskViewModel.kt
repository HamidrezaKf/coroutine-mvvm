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



    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))

    }

    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
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

    fun onAddEditResult(result:Int){
        when(result){
            0 -> showTaskSavedConfirmationMessage("Task added")
            1 -> showTaskSavedConfirmationMessage("Task updated")
        }
    }

    private fun showTaskSavedConfirmationMessage(msg: String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(msg))
    }

    fun onDeleteAllCompletedClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToDeleteAllCompletedScreen)
    }

    fun onDeleteResult(result: Int) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowDeleteCompletedTaskMessage("Completed Tasks Deleted"))
    }

    sealed class TasksEvent{
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowUndoDeleteTaskMessage(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String) : TasksEvent()
        data class ShowDeleteCompletedTaskMessage(val msg: String) : TasksEvent()
        object NavigateToDeleteAllCompletedScreen : TasksEvent()
    }

    data class Orders(val searchQuery: String, val sortOrder: SortOrder, val hideCompleted: Boolean)
}