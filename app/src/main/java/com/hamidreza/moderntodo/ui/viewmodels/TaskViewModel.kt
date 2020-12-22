package com.hamidreza.moderntodo.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hamidreza.moderntodo.data.db.TaskDao


class TaskViewModel @ViewModelInject constructor(private val dao: TaskDao) : ViewModel() {

    val getTasks = dao.getTasks().asLiveData()

}