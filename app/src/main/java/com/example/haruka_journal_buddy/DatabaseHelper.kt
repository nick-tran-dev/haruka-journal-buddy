package com.example.haruka_journal_buddy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import android.database.Cursor
import android.icu.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val selectAll: String = "SELECT * FROM user_entries"
    val selectAllModOrdered: String = "SELECT * FROM user_entries ORDER BY datetime_last_modified DESC"

    private val settingsList = mapOf(
        "done_first_time" to "0"
        ,"user_name" to "Nicole"
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS user_entries (" +
                "entry_id INTEGER PRIMARY KEY" +
                ",prompt_id TEXT NOT NULL" +
                ",prompt TEXT" +
                ",entry TEXT" +
                ",icon_filename TEXT" +
                ",datetime_created DATETIME" +
                ",datetime_last_modified DATETIME" +
            ")"
        )

        db?.execSQL("CREATE TABLE IF NOT EXISTS user_settings (" +
                "setting_id TEXT PRIMARY KEY" +
                ",value TEXT" +
                ",default_value" +
                ")"
        )

        firstTimeSetup()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db?.execSQL("DROP TABLE IF EXISTS user_settings")
        onCreate(db)
    }

    fun WIPEDATABASE() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS user_entries")
        db?.execSQL("DROP TABLE IF EXISTS user_settings")
        onCreate(db)
    }

    fun selectStrFromDb(queryType: String, id: Any?, column: String?): String?{
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
            "setting_by_id" -> this.readableDatabase.rawQuery(
                "SELECT value FROM user_settings WHERE setting_id = ?"
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

    fun selectIntFromDb(queryType: String, id: Any?, column: String?): Int?{
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
            "UPDATE user_entries SET $elementChoose = ?, datetime_last_modified = ? WHERE entry_id = ?"
            ,arrayOf(inputElement, now(), id)
        )
    }

    fun promptExists(promptId: String): Boolean{
        val db = this.readableDatabase
        val exists : Boolean

        val cursor : Cursor = db.rawQuery(
            "SELECT prompt_id, prompt FROM user_entries WHERE prompt_id = ?"
            ,arrayOf(promptId)
        )

        exists = (cursor.count != 0)

        return exists
    }

    fun getSelectResults(inputQuery: String): List<Map<String, Any?>>{
        val result = mutableListOf<Map<String, Any?>>()
        val cursor = this.readableDatabase.rawQuery(inputQuery, null)

        cursor.use{if(cursor.moveToFirst()){
            do{
                val tuple = mutableMapOf<String, Any?>()

                for (columnIndex in 0 until cursor.columnCount)
                    tuple[cursor.getColumnName(columnIndex)] = cursor.getString(columnIndex)

                result.add(tuple)
            } while (cursor.moveToNext())
        }}

        return result
    }

    fun updateSetting(setting: String, inputValue: String){
        this.writableDatabase.execSQL(
            "UPDATE user_settings SET value = ? WHERE setting_id = ?"
            ,arrayOf(inputValue, setting)
        )
    }

    fun now(): String{
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
    }

    private fun firstTimeSetup(){
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery(
            "SELECT setting_id FROM user_settings WHERE setting_id = 'done_first_time' AND value = '0'"
            ,null
        )

        if (cursor.count > 0){
            cursor.close()
            return
        }
        else
            cursor.close()

        for ((key, value) in settingsList)
            db.execSQL(
                "INSERT INTO user_settings (setting_id, value, default_value) VALUES (?, ?, ?)"
                ,arrayOf(key, value, value)
            )

    }

    companion object {
        const val DATABASE_NAME = "user_entries.db"
        const val DATABASE_VERSION = 1
    }
}