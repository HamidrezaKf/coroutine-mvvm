package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


class TaskViewModel @ViewModelInject constructor(private val dao: TaskDao) : ViewModel() {

    val currentSearchQuery = MutableStateFlow("")

    private val tasksFlow = currentSearchQuery.flatMapLatest {
        dao.getTasks(it)
    }

    val getTasks = tasksFlow.asLiveData()

    fun saveTask(task: Task) = viewModelScope.launch {
        dao.saveTask(task)
    }

}