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

        var dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)


        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
       dbHelper.WIPEDATABASE()
        /*
        *
        * */

        val entryDb = dbHelper.writableDatabase
        val entryBody = findViewById<EditText>(R.id.entry_body)

        testInsert1(entryDb)

        var currentEntry : Int? = dbHelper.selectIntFromDb("top_entry", null, "max_entry_id")

        Log.d(currentEntry.toString(), "currentEntry main")

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

        //dbHelper.checkPrompt("tst_new", "this is my insertion prompt")

        startActivity(Intent(this, EntryListActivity::class.java))
    }

    private fun setCurrentPrompt(inputString: String?) {
        //setContentView(R.layout.textview_test)
        val textViewTest: TextView = findViewById<TextView>(R.id.textview_test)
        textViewTest.text = inputString
    }

    private fun testInsert1(db : SQLiteDatabase){
        db.insert("user_entries", null, TestEntries.testValues1)
        db.insert("user_entries", null, TestEntries.testValues2)
    }
}