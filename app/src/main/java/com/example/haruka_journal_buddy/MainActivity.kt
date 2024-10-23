package com.example.haruka_journal_buddy

import android.content.ContentValues
import android.os.Bundle
import android.service.autofill.UserData
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.example.haruka_journal_buddy.R
import kotlinx.coroutines.launch

//import com.example.haruka_prototype.R

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

        val testValues = ContentValues().apply{
            put("entry_id", 4)
            put("prompt_id", "tst1")
            put("prompt", "What's a good meal you've had recently?")
            put("entry", "this is the entry before I write stuff.")
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

        var currentEntry = setCurrentEntry()

        entryBody.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                dbHelper.updateById(currentEntry, "entry", entryBody.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


        setCurrentPrompt(dbHelper.getElementById(currentEntry, "prompt"))
        entryBody.setText(dbHelper.getElementById(currentEntry, "entry"))

        dbHelper.checkPrompt("tst_new", "this is my insertion prompt")


    }

    private fun setCurrentEntry() : Int {
        var currentEntry : Int = -1

        lifecycleScope.launch{
            currentEntry =
                getDailyPrompt()
                    ?: UserDataSingleton.getValue(this@MainActivity, UserDataSingleton.LAST_ENTRY)
                    ?: currentEntry
        }

        return currentEntry
    }

    private fun getDailyPrompt() : Int? {
        return 10 // insert later
    }

    private fun setCurrentPrompt(inputString: String?) {
        //setContentView(R.layout.textview_test)
        val textViewTest: TextView = findViewById<TextView>(R.id.textview_test)
        textViewTest.text = inputString
    }
}