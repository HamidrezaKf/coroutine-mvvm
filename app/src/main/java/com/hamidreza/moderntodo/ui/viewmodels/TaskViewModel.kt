package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hamidreza.moderntodo.data.db.Task
import com.hamidreza.moderntodo.data.db.TaskDao
import com.hamidreza.moderntodo.utils.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
class TaskViewModel @ViewModelInject constructor(private val dao: TaskDao) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val hideCompleted = MutableStateFlow(false)

    private val tasksFlow = combine(
        searchQuery,sortOrder,hideCompleted
    ){
        searchQuery,sortOrder,hideCompleted ->
        Orders(searchQuery, sortOrder, hideCompleted)
    }.flatMapLatest {
        dao.getTasks(it.searchQuery,it.sortOrder,it.hideCompleted)
    }

    val getTasks = tasksFlow.asLiveData()

    fun saveTask(task: Task) = viewModelScope.launch {
        dao.saveTask(task)
    }

    data class Orders(val searchQuery:String, val sortOrder: SortOrder, val hideCompleted:Boolean )
}