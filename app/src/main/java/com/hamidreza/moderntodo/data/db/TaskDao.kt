package com.hamidreza.moderntodo.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task WHERE name LIKE '%' || :query || '%' ORDER BY important DESC")
    fun getTasks(query:String) : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)
}