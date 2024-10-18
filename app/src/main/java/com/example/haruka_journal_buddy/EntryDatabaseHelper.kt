package com.example.haruka_journal_buddy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.database.Cursor

class EntryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS user_entries (" +
                "entry_id INTEGER PRIMARY KEY" +
                ",prompt_id TEXT NOT NULL" +
                ",prompt TEXT" +
                ",entry TEXT" +
                ",datetime DATETIME" +
                ")"
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db?.execSQL("DROP TABLE IF EXISTS user_entries")
        onCreate(db)
    }

    fun WIPEDATABASE() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS user_entries")
        onCreate(db)
    }

    fun getElementById(id: Int, elementChoose: String): String?{
        if (elementChoose !in listOf("prompt", "entry", "datetime")) {return "ERROR: Invalid SELECT query"}

        val db = this.readableDatabase

        val cursor : Cursor = db.rawQuery(
            "SELECT "
                + elementChoose
                + " FROM user_entries WHERE entry_id = ?"
            ,arrayOf(id.toString())
        )

        var result: String? = null

        if (cursor.moveToFirst())
            result = cursor.getString(cursor.getColumnIndexOrThrow(elementChoose))

        cursor.close()
        return result
    }

    fun updateById(id: Int, elementChoose: String, inputElement: String?){
        if (elementChoose !in listOf("prompt", "entry", "datetime")) {return}

        this.readableDatabase.execSQL(
            "UPDATE user_entries SET "
            + elementChoose + " = "
            + "\"" + inputElement + "\""
            + " WHERE entry_id = " + id
        )
    }

    fun checkPrompt(promptId : String, inputPrompt : String) : Boolean {
        val db = this.readableDatabase

        val cursor : Cursor = db.rawQuery(
            "SELECT prompt_id, prompt FROM user_entries WHERE prompt_id = ?"
            ,arrayOf(promptId)
        )

        if (cursor.count == 0){
            db.execSQL(
                "INSERT INTO user_entries (prompt_id, prompt, datetime) VALUES (?, ?, ?)"
                ,arrayOf(promptId, inputPrompt, System.currentTimeMillis())
            )
            return false
        }

        cursor.close()
        return true
    }

    companion object {
        const val DATABASE_NAME = "user_entries.db"
        const val DATABASE_VERSION = 1
    }
}