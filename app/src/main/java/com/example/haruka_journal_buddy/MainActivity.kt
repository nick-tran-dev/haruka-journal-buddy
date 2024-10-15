package com.example.haruka_journal_buddy

import android.content.ContentValues
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)


        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
        dbHelper.WIPEDATABASE()

        val entryDb = dbHelper.writableDatabase
        val testValues = ContentValues().apply{
            put("entry_id", 4)
            put("prompt_id", "tst1")
            put("prompt", "This is a test journal prompt")
            put("entry", "i have changed what i'm writing about.")
            put("datetime", System.currentTimeMillis())
        }

        val testValues2 = ContentValues().apply{
            put("entry_id", 10)
            put("prompt_id", "tst2")
            put("prompt", "My SECOND test prompt")
            put("entry", "today, I had raising canes. it was yummy.")
            put("datetime", System.currentTimeMillis())
        }

        entryDb.insert("user_entries", null, testValues)
        entryDb.insert("user_entries", null, testValues2)

        setTestText(dbHelper.getElementById(4, "entry"))
    }

    private fun setTestText(inputString: String?) {
        //setContentView(R.layout.textview_test)
        val textViewTest: TextView = findViewById<TextView>(R.id.textview_test)

        textViewTest.text = inputString
    }
}