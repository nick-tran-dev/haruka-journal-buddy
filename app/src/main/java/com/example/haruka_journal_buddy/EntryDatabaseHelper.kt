package com.example.haruka_journal_buddy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.database.Cursor

class EntryDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS user_entries (" +
                "entry_id INTEGER PRIMARY KEY" +
                ",prompt_id TEXT NOT NULL" +
                ",prompt TEXT" +
                ",entry TEXT" +
                ",datetime DATETIME" +
            ")"
        )
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

    fun selectStrFromDb(queryType: String, id: Any?,column: String?): String?{
        if (queryType == "element_by_prompt_id" && column !in listOf("prompt", "entry", "datetime"))
            return null

        val cursor : Cursor? = when(queryType){
            "element_by_prompt_id" -> this.readableDatabase.rawQuery(
                "SELECT $column FROM user_entries WHERE entry_id = ?"
                ,arrayOf(id.toString())
            )
            "entry_by_prompt_id" -> this.readableDatabase.rawQuery(
                "SELECT entry_id FROM user_entries WHERE prompt_id = ?"
                ,arrayOf(id.toString())
            )
            else -> null
        }

        val result = if (cursor != null && cursor.moveToFirst())
            cursor.getString(cursor.getColumnIndexOrThrow(column))
        else
            null

        cursor?.close()
        return result
    }

    fun selectIntFromDb(queryType: String, id: Any?,column: String?): Int?{
        val cursor : Cursor? = when(queryType){
            "top_entry" -> this.readableDatabase.rawQuery(
                "SELECT MAX(entry_id) AS 'max_entry_id' FROM user_entries"
                ,null
            )
            else -> null
        }

        val result : Int?

        if (cursor != null && cursor.moveToFirst())
           result = cursor.getInt(cursor.getColumnIndexOrThrow(column))
        else
            result = null

        cursor?.close()
        return result
    }

    fun updateById(id: Int, elementChoose: String, inputElement: String?){
        if (elementChoose !in listOf("prompt", "entry", "datetime"))
            return

        this.readableDatabase.execSQL(
            "UPDATE user_entries SET $elementChoose = ? WHERE entry_id = ?"
            ,arrayOf(inputElement, id)
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