package com.example.matthewrioslayout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonCreateAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This uses your login screen layout
        setContentView(R.layout.activity_login)

        databaseHelper = DatabaseHelper(this)

        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount)

        buttonLogin.setOnClickListener {
            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                val validUser = databaseHelper.checkUser(username, password)

                if (validUser) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WeightActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}