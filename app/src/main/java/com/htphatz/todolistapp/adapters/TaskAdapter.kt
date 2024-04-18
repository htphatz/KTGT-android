package com.htphatz.todolistapp.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.htphatz.todolistapp.R
import com.htphatz.todolistapp.activities.UpdateActivity
import com.htphatz.todolistapp.db.DbHelper
import com.htphatz.todolistapp.model.Task

class TaskAdapter(var context: Activity, var resource: Int, objects: List<Task>) :
    ArrayAdapter<Task>(context, resource, objects) {
    var objects: List<Task>
    private lateinit var db: DbHelper
    lateinit var listener: OnItemClickListener

    init {
        this.objects = objects
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val row = inflater.inflate(resource, null)
        val task: Task = objects[position]
        db = DbHelper(context,null)

        // Map view
        val txtName = row.findViewById<TextView>(R.id.txtName)
        val txtDescription = row.findViewById<TextView>(R.id.txtDescription)
        val btnUpdate = row.findViewById<ImageButton>(R.id.btnUpdate)
        val btnDelete = row.findViewById<ImageButton>(R.id.btnDelete)

        // Show data
        txtName.text = task.name
        txtDescription.text = task.description

        btnUpdate.setOnClickListener {
            val intent = Intent(context, UpdateActivity::class.java)
            intent.putExtra("task_id", task.id)
            context.startActivity(intent)
        }

        btnDelete.setOnClickListener {
            listener!!.onItemClick(task)
        }
        return row
    }
    interface OnItemClickListener {
        fun onItemClick(task: Task)
    }

}