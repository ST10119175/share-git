package com.example.sqllite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    // Data class for User
    data class User(
        val id: Long = -1,
        val name: String,
        val email: String
    )

    companion object {
        private const val DATABASE_NAME = "UserDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"

        // Column names
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"

        // Create table query
        private const val CREATE_TABLE = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the database table
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop older table if exists and create fresh
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // CRUD Operations

    // Create - Insert new user
    fun insertUser(user: User): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
        }

        // Insert row and return row ID
        return db.insert(TABLE_NAME, null, values).also {
            db.close()
        }
    }

    // Read - Get all users
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        with(cursor) {
            while (moveToNext()) {
                val user = User(
                    id = getLong(getColumnIndexOrThrow(COLUMN_ID)),
                    name = getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    email = getString(getColumnIndexOrThrow(COLUMN_EMAIL))
                )
                users.add(user)
            }
            close()
        }
        return users
    }

    // Read - Get single user
    fun getUser(id: Long): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COLUMN_ID = ?",
            arrayOf(id.toString()), null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                User(
                    id = it.getLong(it.getColumnIndexOrThrow(COLUMN_ID)),
                    name = it.getString(it.getColumnIndexOrThrow(COLUMN_NAME)),
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL))
                )
            } else null
        }
    }

    // Update user
    fun updateUser(user: User): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, user.name)
            put(COLUMN_EMAIL, user.email)
        }

        // Update row and return number of rows affected
        return db.update(
            TABLE_NAME, values,
            "$COLUMN_ID = ?", arrayOf(user.id.toString())
        )
    }

    // Delete user
    fun deleteUser(id: Long): Int {
        val db = this.writableDatabase
        // Delete row and return number of rows affected
        return db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?", arrayOf(id.toString())
        )
    }
}