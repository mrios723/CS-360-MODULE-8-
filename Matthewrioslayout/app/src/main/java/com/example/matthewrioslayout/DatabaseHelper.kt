package com.example.matthewrioslayout

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "WeightTracker.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val USER_ID = "id"
        private const val USERNAME = "username"
        private const val PASSWORD = "password"

        private const val TABLE_WEIGHTS = "weights"
        private const val WEIGHT_ID = "id"
        private const val WEIGHT_DATE = "date"
        private const val WEIGHT_VALUE = "weight"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $USERNAME TEXT UNIQUE,
                $PASSWORD TEXT
            )
        """.trimIndent()

        val createWeightsTable = """
            CREATE TABLE $TABLE_WEIGHTS (
                $WEIGHT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $WEIGHT_DATE TEXT,
                $WEIGHT_VALUE REAL
            )
        """.trimIndent()

        db.execSQL(createUsersTable)
        db.execSQL(createWeightsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEIGHTS")
        onCreate(db)
    }

    // Creates a new user account
    fun addUser(username: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(USERNAME, username)
        values.put(PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, values)
        return result != -1L
    }

    // Checks login username and password
    fun checkUser(username: String, password: String): Boolean {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_USERS WHERE $USERNAME = ? AND $PASSWORD = ?",
            arrayOf(username, password)
        )

        val exists = cursor.count > 0
        cursor.close()

        return exists
    }

    // Adds a new weight entry
    fun addWeight(date: String, weight: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(WEIGHT_DATE, date)
        values.put(WEIGHT_VALUE, weight)

        val result = db.insert(TABLE_WEIGHTS, null, values)
        return result != -1L
    }

    // Reads all weight entries
    fun getAllWeights(): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT * FROM $TABLE_WEIGHTS ORDER BY $WEIGHT_ID DESC",
            null
        )
    }

    // Updates a weight entry by ID
    fun updateWeight(id: Int, date: String, weight: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(WEIGHT_DATE, date)
        values.put(WEIGHT_VALUE, weight)

        val result = db.update(
            TABLE_WEIGHTS,
            values,
            "$WEIGHT_ID = ?",
            arrayOf(id.toString())
        )

        return result > 0
    }

    // Deletes a weight entry by ID
    fun deleteWeight(id: Int): Boolean {
        val db = writableDatabase

        val result = db.delete(
            TABLE_WEIGHTS,
            "$WEIGHT_ID = ?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}