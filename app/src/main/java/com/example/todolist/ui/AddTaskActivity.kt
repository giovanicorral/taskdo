package com.example.todolist.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.todolist.R
import com.example.todolist.databinding.ActivityAddTaskBinding
import com.example.todolist.datasource.TaskDataSource
import com.example.todolist.extensions.format
import com.example.todolist.extensions.text
import com.example.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import javax.security.auth.login.LoginException

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour
                binding.tilDescription.text = it.description
            }
        }

        insertListeners()
    }


    private fun insertListeners() {
        binding.tilDate.editText?.setOnClickListener {
            val datePiker = MaterialDatePicker.Builder.datePicker().build()
            datePiker.addOnPositiveButtonClickListener {
                binding.tilDate.text = Date(it).format()
            }
            datePiker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tilHour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val minute =
                    if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour

                binding.tilHour.text = "$hour:$minute"

            }
            timePicker.show(supportFragmentManager, null)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnNewTask.setOnClickListener {
            var error = false
            if (binding.tilTitle.text.isEmpty()) {
                binding.tilTitle.error = "Obrigatório"
                error = true
            } else {
                binding.tilTitle.error = null
            }
            if (binding.tilDate.text.isEmpty()) {
                binding.tilDate.error = "Obrigatório"
                error = true
            } else {
                binding.tilTitle.error = null
            }
            if (!error) {
                val task = Task(
                    title = binding.tilTitle.text,
                    date = binding.tilDate.text,
                    hour = binding.tilHour.text,
                    description = binding.tilDescription.text,
                    id = intent.getIntExtra(TASK_ID, 0)
                )


                TaskDataSource.insertTask(task)
                setResult(RESULT_OK)
                Toast.makeText(this, "Tarefa incluida", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        binding.toolbar.setOnClickListener {
            finish()
        }
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}