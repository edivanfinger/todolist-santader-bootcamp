package br.com.dio.todolist.repositories

import android.content.Context
import br.com.dio.todolist.database.TaskDatabase
import br.com.dio.todolist.model.Task

class TaskRepository {

    suspend fun insertTask(ctx: Context, item: Task){
        TaskDatabase(ctx).getDao().insertTask(item)
    }

    suspend fun selectTasks(ctx: Context): List<Task> {
        return TaskDatabase(ctx).getDao().selectTasks()
    }

    suspend fun selectTaskById(ctx: Context,id:Int): List<Task> {
        return TaskDatabase(ctx).getDao().selectTaskById(id)
    }

    suspend fun updateTask(ctx: Context,id:Int,title:String,hour:String,date:String){
        TaskDatabase(ctx).getDao().updateTask(id, title, hour, date)
    }

    suspend fun deleteTask(ctx: Context,id:Int){
        TaskDatabase(ctx).getDao().deleteTask(id)
    }
}