package com.hamidreza.moderntodo.data.db

import androidx.room.*
import com.hamidreza.moderntodo.utils.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Dao
interface TaskDao {

    fun getTasks(query: String,sortOrder: SortOrder,hideCompleted: Boolean) : Flow<List<Task>> {
        return when(sortOrder){
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
            SortOrder.BY_DATE -> getTasksSortedByName(query, hideCompleted)
        }
    }

    @Query("SELECT * FROM Task WHERE (completed != :hideCompleted OR completed =0) AND name LIKE '%' || :query || '%' ORDER BY important,name DESC")
    fun getTasksSortedByName(query:String,hideCompleted:Boolean) : Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE (completed != :hideCompleted OR completed =0) AND name LIKE '%' || :query || '%' ORDER BY important,created DESC")
    fun getTasksSortedByDateCreated(query:String,hideCompleted:Boolean) : Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)
}