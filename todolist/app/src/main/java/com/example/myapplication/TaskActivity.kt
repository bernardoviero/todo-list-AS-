package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter

class TaskActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: ArrayAdapter<String>
    private var taskList: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        dbHelper = DBHelper(this)

        taskListView = findViewById(R.id.taskListView)
        taskAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList)
        taskListView.adapter = taskAdapter

        val userId = intent.getIntExtra("USER_ID", 0)
        loadTasks(userId)

        val taskInput = findViewById<EditText>(R.id.taskInput)
        val addButton = findViewById<Button>(R.id.addButton)

        addButton.setOnClickListener {
            val taskDescription = taskInput.text.toString()
            if (taskDescription.isNotEmpty()) {
                addTask(taskDescription, userId)
                taskInput.text.clear()
            }
        }
    }

    private fun loadTasks(userId: Int) {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_TASKS} WHERE ${DBHelper.COLUMN_TASK_USER_ID} = ?", arrayOf(userId.toString()))
        taskList.clear()
        if (cursor.moveToFirst()) {
            do {
                val task = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TASK_DESCRIPTION))
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        taskAdapter.notifyDataSetChanged()
    }

    private fun addTask(description: String, userId: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_TASK_DESCRIPTION, description)
            put(DBHelper.COLUMN_TASK_STATUS, 0)
            put(DBHelper.COLUMN_TASK_USER_ID, userId)
        }
        db.insert(DBHelper.TABLE_TASKS, null, values)
        loadTasks(userId)
    }
}
