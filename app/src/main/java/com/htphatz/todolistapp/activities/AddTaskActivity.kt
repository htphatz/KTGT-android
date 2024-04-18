package com.htphatz.todolistapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.htphatz.todolistapp.MainActivity
import com.htphatz.todolistapp.databinding.ActivityAddTaskBinding
import com.htphatz.todolistapp.db.DbHelper
import com.htphatz.todolistapp.model.Task

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private lateinit var db: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        db = DbHelper(this, null)

        addTask()
    }

    private fun addTask() {
        binding.btnSave.setOnClickListener {
            val task = Task (
                0,
                binding.txtName.text.toString(),
                binding.txtDescription.text.toString()
            )
            val rs = db.addTask(task)
            if (rs >= 1) Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show()
            else {
                Toast.makeText(this, "Cannot save: ${task.name}", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}