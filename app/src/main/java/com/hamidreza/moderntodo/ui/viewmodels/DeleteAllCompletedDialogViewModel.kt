package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class DeleteAllCompletedDialogViewModel @ViewModelInject constructor(
    private val dao: TaskDao,
) : ViewModel() {


    fun onConfirmClick() = viewModelScope.launch {
        dao.deleteCompletedTasks()
    }


}