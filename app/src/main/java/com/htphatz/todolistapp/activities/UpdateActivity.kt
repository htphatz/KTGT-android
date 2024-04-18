package com.htphatz.todolistapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import com.htphatz.todolistapp.R
import com.htphatz.todolistapp.adapters.TaskAdapter
import com.htphatz.todolistapp.databinding.ActivityUpdateBinding
import com.htphatz.todolistapp.db.DbHelper
import com.htphatz.todolistapp.model.Task

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private lateinit var db: DbHelper
    private var id: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = DbHelper(this, null)

        id = intent.getIntExtra("task_id", -1)
        if (id === -1) {
            finish()
        }


        val task = db.getTaskById(id)
        binding.txtUpdateName.setText(task?.name)
        binding.txtUpdateDescription.setText(task?.description)

        binding.btnUpdate.setOnClickListener {
            val newName = binding.txtUpdateName.text.toString()
            val newDescription = binding.txtUpdateDescription.text.toString()
            val updatedTask = Task(id, newName, newDescription)
            val rs = db.updateTask(updatedTask)
            finish()
            if (rs >= 1){
                Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Cannot updated: ${task?.name}", Toast.LENGTH_SHORT).show()
            }
        }

    }
}