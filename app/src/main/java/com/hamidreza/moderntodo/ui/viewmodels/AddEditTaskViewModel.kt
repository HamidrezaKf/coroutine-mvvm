package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao

class AddEditTaskViewModel @ViewModelInject constructor(
    private val dao:TaskDao,
    @Assisted private val state:SavedStateHandle
):ViewModel() {

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

}