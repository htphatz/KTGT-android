package com.htphatz.todolistapp.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.htphatz.todolistapp.model.Task

class DbHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "to-do-list"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "tasks"

        // Column name for table
        const val id_col = "id"
        const val name_col = "name"
        const val description_col = "description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = ("CREATE TABLE " + TABLE_NAME + " (" +
                id_col + " INTEGER PRIMARY KEY, " +
                name_col + " TEXT, " +
                description_col + " TEXT" + ")")
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Create task
    fun addTask(task: Task): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(name_col, task.name)
            put(description_col, task.description)
        }
        val rs = db.insert(TABLE_NAME, null, values)
        db.close()
        return rs
    }

    // Get all tasks
    @SuppressLint("Range")
    fun readTasks(): List<Task> {
        val rs: ArrayList<Task> = ArrayList()
        val sql = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(sql, null)
        } catch (e: SQLException) {
            // Handle SQL exception
            db.close()
            return ArrayList()
        }

        cursor?.use {
            // Iterate over cursor
            while (it.moveToNext()) {
                val task = Task(
                    it.getInt(it.getColumnIndexOrThrow(id_col)),
                    it.getString(it.getColumnIndexOrThrow(name_col)),
                    it.getString(it.getColumnIndexOrThrow(description_col))
                )
                rs.add(task)
            }
        }

        db.close()
        return rs
    }

    @SuppressLint("Range")
    fun getTaskById(id: Int): Task? {
        val db = this.readableDatabase
        val sql = "SELECT * FROM $TABLE_NAME WHERE $id_col = ?"
        val cursor = db.rawQuery(sql, arrayOf(id.toString()))

        var task: Task? = null

        cursor.use {
            if (it.moveToFirst()) {
                task = Task(
                    it.getInt(it.getColumnIndexOrThrow(id_col)),
                    it.getString(it.getColumnIndexOrThrow(name_col)),
                    it.getString(it.getColumnIndexOrThrow(description_col))
                )
            }
        }

        cursor.close()
        db.close()
        return task
    }

// Update task by name
fun updateTask(task: Task): Int {
    val db = this.writableDatabase
    val values = ContentValues().apply {
        put(name_col, task.name)
        put(description_col, task.description)
    }
    val whereClause = "${id_col} = ?"
    val whereArgs = arrayOf(task.id.toString())
    val rs = db.update(TABLE_NAME, values, whereClause, whereArgs)
    db.close()
    return rs
}

// Delete task by name
fun deleteTaskById(task: Task): Int {
    val db = this.writableDatabase
    val rs = db.delete(TABLE_NAME, "$id_col LIKE ?", arrayOf(task.id.toString()))
    db.close()
    return rs
    }
}