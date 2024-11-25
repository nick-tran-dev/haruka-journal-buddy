package com.example.haruka_journal_buddy

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
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

        val passedString = intent.getStringExtra("EXTRA_STRING")
        val currentEntry = passedString?.toIntOrNull()

        val dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)
        //val entryDb = dbHelper.writableDatabase
        val entryBody = findViewById<EditText>(R.id.entry_body)

        entryBody.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                dbHelper.updateById(
                    currentEntry ?: 1, "entry", entryBody.text.toString()
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        setCurrentPrompt(
            dbHelper.selectStrFromDb("element_by_prompt_id", currentEntry, "prompt")
        )
        entryBody.setText(
            dbHelper.selectStrFromDb("element_by_prompt_id", currentEntry, "entry")
        )

    }

    private fun setCurrentPrompt(inputString: String?) {
        val textViewTest: TextView = findViewById<TextView>(R.id.textview_test)
        textViewTest.text = inputString
    }
}