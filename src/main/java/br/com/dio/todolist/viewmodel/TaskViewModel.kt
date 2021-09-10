package br.com.dio.todolist.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import br.com.dio.todolist.model.Task
import br.com.dio.todolist.repositories.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class TaskViewModel (private val ctx: Context, app:Application) : AndroidViewModel(app) {

    val errorMessage = MutableLiveData<String>()
    var dataList = MutableLiveData<List<Task>>()
    var dataTask = MutableLiveData<Task>()

    init {
        selectTasks()
    }

    fun selectTasks(){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = TaskRepository().selectTasks(ctx)
                dataList.postValue(result)
            }catch (e: Exception){
                errorMessage.postValue(e.message)
            }
        }
    }

    fun selectTaskById(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = TaskRepository().selectTaskById(ctx,id)
                dataTask.postValue(result[0])
            }catch (e: Exception){
                errorMessage.postValue(e.message)
            }
        }
    }

    fun insertTask(item:Task){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                TaskRepository().insertTask(ctx,item)
            }catch (e: Exception){
                errorMessage.postValue(e.message)
            }
        }
    }

    fun updateTask(item:Task){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                TaskRepository().updateTask(ctx,item.id!!,item.title,item.hour,item.date)
            }catch (e: Exception){
                errorMessage.postValue(e.message)
            }
        }
    }
    fun deleteTask(id:Int){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                TaskRepository().deleteTask(ctx,id)
                selectTasks()
            }catch (e: Exception){
                errorMessage.postValue(e.message)
            }
        }
    }
}