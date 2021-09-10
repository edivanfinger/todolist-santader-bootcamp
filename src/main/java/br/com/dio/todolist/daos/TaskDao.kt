package br.com.dio.todolist.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.dio.todolist.model.Task

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task")
    suspend fun selectTasks():List<Task>

    @Query("SELECT * FROM task WHERE id = :id")
    suspend fun selectTaskById(id:Int):List<Task>

    @Query("UPDATE task SET title = :title, hour = :hour, date = :date WHERE id = :id")
    suspend fun updateTask(id:Int,title:String,hour:String,date:String)

    @Query("DELETE FROM task WHERE id = :id")
    suspend fun deleteTask(id:Int)

}