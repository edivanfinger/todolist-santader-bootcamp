package br.com.dio.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.datasource.TaskDataSource
import br.com.dio.todolist.model.Task
import br.com.dio.todolist.viewmodel.TaskViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskListAdapter
    private lateinit var viewModel: TaskViewModel
    private lateinit var dataList: MutableList<Task>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataList = ArrayList()
        viewModel = TaskViewModel(this,this.application)
        adapter = TaskListAdapter(this,dataList,viewModel)
        binding.rvTasks.adapter = adapter

        insertListeners()
        insertObservers()
        updateList()
    }

    private fun insertObservers() {
        viewModel.dataList.observe(this, Observer{
            dataList.clear()
            for (i in it){
                dataList.add(i)
            }
            adapter.notifyDataSetChanged()
            binding.includeEmpty.emptyState.visibility = if (it.isEmpty()) View.VISIBLE
            else View.GONE
        })
        viewModel.errorMessage.observe(this,Observer{
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        })
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()
    }

    private fun updateList() {
        viewModel.selectTasks()
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

}