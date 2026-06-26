package com.example.matthewrioslayout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WeightActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var editDate: EditText
    private lateinit var editWeight: EditText
    private lateinit var editId: EditText
    private lateinit var buttonAdd: Button
    private lateinit var buttonUpdate: Button
    private lateinit var buttonDelete: Button
    private lateinit var buttonSms: Button
    private lateinit var textResults: TextView

    private val smsPermissionCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_weight_grid)

        databaseHelper = DatabaseHelper(this)

        editDate = findViewById(R.id.editDate)
        editWeight = findViewById(R.id.editWeight)
        editId = findViewById(R.id.editId)

        buttonAdd = findViewById(R.id.buttonAdd)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        buttonDelete = findViewById(R.id.buttonDelete)
        buttonSms = findViewById(R.id.buttonSms)

        textResults = findViewById(R.id.textResults)

        displayWeights()

        buttonAdd.setOnClickListener {
            val date = editDate.text.toString().trim()
            val weightText = editWeight.text.toString().trim()

            if (date.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(this, "Enter date and weight", Toast.LENGTH_SHORT).show()
            } else {
                val success = databaseHelper.addWeight(date, weightText.toDouble())

                if (success) {
                    Toast.makeText(this, "Weight added", Toast.LENGTH_SHORT).show()
                    clearFields()
                    displayWeights()
                    checkGoalWeight(weightText.toDouble())
                }
            }
        }

        buttonUpdate.setOnClickListener {
            val idText = editId.text.toString().trim()
            val date = editDate.text.toString().trim()
            val weightText = editWeight.text.toString().trim()

            if (idText.isEmpty() || date.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(this, "Enter ID, date, and weight", Toast.LENGTH_SHORT).show()
            } else {
                val success = databaseHelper.updateWeight(
                    idText.toInt(),
                    date,
                    weightText.toDouble()
                )

                if (success) {
                    Toast.makeText(this, "Weight updated", Toast.LENGTH_SHORT).show()
                    clearFields()
                    displayWeights()
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonDelete.setOnClickListener {
            val idText = editId.text.toString().trim()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Enter ID to delete", Toast.LENGTH_SHORT).show()
            } else {
                val success = databaseHelper.deleteWeight(idText.toInt())

                if (success) {
                    Toast.makeText(this, "Weight deleted", Toast.LENGTH_SHORT).show()
                    clearFields()
                    displayWeights()
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonSms.setOnClickListener {
            requestSmsPermission()
        }
    }

    private fun displayWeights() {
        val cursor = databaseHelper.getAllWeights()
        val builder = StringBuilder()

        builder.append("ID     Date     Weight\n")
        builder.append("-----------------------------\n")

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val date = cursor.getString(1)
            val weight = cursor.getDouble(2)

            builder.append("$id     $date     $weight lbs\n")
        }

        cursor.close()
        textResults.text = builder.toString()
    }

    private fun clearFields() {
        editId.text.clear()
        editDate.text.clear()
        editWeight.text.clear()
    }

    private fun requestSmsPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.SEND_SMS),
                smsPermissionCode
            )
        } else {
            Toast.makeText(this, "SMS permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkGoalWeight(weight: Double) {
        val goalWeight = 180.0

        if (weight <= goalWeight) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.SEND_SMS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                try {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(
                        "5551234567",
                        null,
                        "Congratulations! You reached your goal weight.",
                        null,
                        null
                    )
                } catch (e: Exception) {
                    Toast.makeText(this, "SMS alert could not be sent", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Goal reached, but SMS permission was denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}