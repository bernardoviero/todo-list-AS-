
package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("RegisterActivity", "RegisterActivity created")
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val email = emailInput.text.toString()
            val name = nameInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
                if (registerUser(email, name, password)) {
                    Log.d("RegisterActivity", "User registered successfully")
                    Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Log.d("RegisterActivity", "Registration failed: Email already exists")
                    Toast.makeText(this, "Erro: Email já cadastrado.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d("RegisterActivity", "Registration failed: Fields missing")
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, name: String, password: String): Boolean {
        val db = dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${DBHelper.TABLE_USERS} WHERE ${DBHelper.COLUMN_USER_NAME}=?", arrayOf(email))
        return if (cursor.count > 0) {
            cursor.close()
            false
        } else {
            cursor.close()
            val values = ContentValues().apply {
                put(DBHelper.COLUMN_USER_NAME, email)
                put(DBHelper.COLUMN_USER_PASSWORD, password)
            }
            db.insert(DBHelper.TABLE_USERS, null, values)
            true
        }
    }
}
