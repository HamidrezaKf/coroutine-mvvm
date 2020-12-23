package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val dao:TaskDao,
    @Assisted private val state:SavedStateHandle
):ViewModel() {

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

    val task = state.get<Task>("task")

    var taskName = state.get<String>("taskName") ?: task?.name ?: ""
        set(value) {
            field = value
            state.set("taskName", value)
        }

    var taskImportance = state.get<Boolean>("taskImportance") ?: task?.important ?: false
        set(value) {
            field = value
            state.set("taskImportance", value)
        }

    fun saveClick(){
        if (taskName.isBlank()){
            showInvalidInputMessage("Name cannot be empty")
            return
        }
        if (task != null){
            val updateTask = task.copy(name = taskName,important = taskImportance)
            updateTask(updateTask)
        }else {
            val task = Task(name = taskName , important = taskImportance)
            createTask(task)
        }

    }

    private fun updateTask(task: Task) = viewModelScope.launch {
        dao.updateTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(1))
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        dao.saveTask(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(0))
    }

    private fun showInvalidInputMessage(msg: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(msg))
    }

    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }

}