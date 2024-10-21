package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LoginActivity", "LoginActivity created")
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val emailInput = findViewById<EditText>(R.id.usernameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            Log.d("LoginActivity", "Attempting login with email: $email")

            try {
                val userId = getUserId(email, password) // Get userId after validating login
                if (userId != null) {
                    Log.d("LoginActivity", "Login successful, moving to TaskActivity")
                    val intent = Intent(this, TaskActivity::class.java)
                    intent.putExtra("USER_ID", userId) // Pass userId to TaskActivity
                    startActivity(intent)
                } else {
                    Log.d("LoginActivity", "Login failed: Invalid email or password")
                    Toast.makeText(this, "Email ou senha inválidos", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error during login: ${e.message}", e)
                Toast.makeText(this, "Erro ao realizar login. Tente novamente.", Toast.LENGTH_LONG).show()
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserId(email: String, password: String): Int? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT ${DBHelper.COLUMN_USER_ID} FROM ${DBHelper.TABLE_USERS} WHERE ${DBHelper.COLUMN_USER_NAME}=? AND ${DBHelper.COLUMN_USER_PASSWORD}=?", arrayOf(email, password))

        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USER_ID))
        }
        cursor.close()
        return userId
    }
}
