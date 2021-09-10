package br.com.dio.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.datasource.TaskDataSource
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import br.com.dio.todolist.model.Task
import br.com.dio.todolist.viewmodel.TaskViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var task: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = TaskViewModel(this,this.application)

        insertObservers()
        insertListeners()

        if (intent.hasExtra(TASK_ID)) {
            viewModel.selectTaskById(intent.getIntExtra(TASK_ID, 0))
        }

    }

    private fun insertObservers() {
        viewModel.dataTask.observe(this, Observer{
            task = it
            binding.tilTitle.text = it.title
            binding.tilDate.text = it.date
            binding.tilHour.text = it.hour
        })
        viewModel.errorMessage.observe(this,Observer{
            Toast.makeText(this,it, Toast.LENGTH_LONG).show()
        })
    }

    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.tilDate.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            var id: Int? = null
            if (intent.hasExtra(TASK_ID)) {
                id = intent.getIntExtra(TASK_ID, 0)
            }
            val task = Task(
                title = binding.tilTitle.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text,
                id = id
            )
            if (intent.hasExtra(TASK_ID)) {
                viewModel.updateTask(task)
            }else{
                viewModel.insertTask(task)
            }
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    companion object {
        const val TASK_ID = "task_id"
    }

}