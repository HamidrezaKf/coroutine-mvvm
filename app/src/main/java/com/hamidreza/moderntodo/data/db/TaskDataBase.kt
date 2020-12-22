package com.hamidreza.moderntodo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Task::class],version = 1)
abstract class TaskDataBase : RoomDatabase() {

    abstract fun taskDao() : TaskDao
}