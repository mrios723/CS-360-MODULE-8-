package com.example.matthewrioslayout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Using your register layout
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {

            val username = editUsername.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Please enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val success = databaseHelper.addUser(
                    username,
                    password
                )

                if (success) {
                    Toast.makeText(
                        this,
                        "Account Created Successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Username Already Exists",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}