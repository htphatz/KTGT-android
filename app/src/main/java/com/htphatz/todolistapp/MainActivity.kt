package com.htphatz.todolistapp

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.htphatz.todolistapp.activities.AddTaskActivity
import com.htphatz.todolistapp.adapters.TaskAdapter
import com.htphatz.todolistapp.databinding.ActivityMainBinding
import com.htphatz.todolistapp.db.DbHelper
import com.htphatz.todolistapp.model.Task

class MainActivity : AppCompatActivity(),TaskAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DbHelper
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = DbHelper(this, null)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivity(intent)
        }

        getList()
    }

    private fun getList(){
        val tasks = db.readTasks()
        // adapter = ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, list)
        taskAdapter = TaskAdapter(this, R.layout.task_item, tasks)
        taskAdapter.setOnItemClickListener(this)
        binding.lsvTasks.adapter = taskAdapter
    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    override fun onItemClick(task: Task) {
        val dlg = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        dlg.setTitle("Delete user")
        dlg.setMessage("Are you sure?")
        dlg.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, which ->
            db.deleteTaskById(task)
            getList()
        })
        dlg.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which ->

        })
        val b = dlg.create()
        b.show()
    }

}