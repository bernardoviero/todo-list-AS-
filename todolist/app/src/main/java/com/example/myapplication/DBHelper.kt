
package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TodoList.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val TABLE_TASKS = "tasks"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_NAME = "username"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_TASK_ID = "task_id"
        const val COLUMN_TASK_DESCRIPTION = "description"
        const val COLUMN_TASK_STATUS = "status"
        const val COLUMN_TASK_USER_ID = "user_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")")
        db?.execSQL(CREATE_USER_TABLE)

        val CREATE_TASK_TABLE = ("CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_STATUS + " INTEGER,"
                + COLUMN_TASK_USER_ID + " INTEGER," +
                " FOREIGN KEY (" + COLUMN_TASK_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" + ")")
        db?.execSQL(CREATE_TASK_TABLE)

        // Insert default user
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, "admin")
            put(COLUMN_USER_PASSWORD, "1234")
        }
        db?.insert(TABLE_USERS, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }
}
