package com.example.haruka_journal_buddy

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EntryListActivity : AppCompatActivity() {
    private val DESC_MAX = 50

    private lateinit var promptRecyclerView: RecyclerView
    private lateinit var entryList: ArrayList<SavedEntry>
    lateinit var entryIds: MutableList<String>
    lateinit var imageIds: MutableList<Int>
    lateinit var headings: MutableList<String>
    lateinit var descs: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuButton: Button = findViewById(R.id.menu_button)
        menuButton.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        val dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)
        val entryDb = dbHelper.writableDatabase

        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
        dbHelper.WIPEDATABASE()
        testInsert1(entryDb)
        /*
        *
        * */

        refreshRecycler()
    }

    override fun onResume(){
        super.onResume()
        refreshRecycler()
    }

    private fun refreshRecycler(){
        entryIds = mutableListOf()
        imageIds = mutableListOf()
        headings = mutableListOf()
        descs = mutableListOf()

        val dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)
        val entryDb = dbHelper.writableDatabase
        val entries = dbHelper.getSelectResults(dbHelper.selectAllModOrdered)

        for (entry in entries){
            entryIds.add(entry["entry_id"].toString())
            addDrawableImage(this, entry["icon_filename"].toString())
            headings.add(entry["prompt"].toString())
            descs.add(entry["entry"].toString())
        }

        promptRecyclerView = findViewById(R.id.prompt_list)
        promptRecyclerView.layoutManager = LinearLayoutManager(this)
        promptRecyclerView.setHasFixedSize(true)

        entryList = arrayListOf<SavedEntry>()

        getUserEntries()
    }

    private fun getUserEntries(){
        for (i in imageIds.indices){
            val savedEntry = SavedEntry(imageIds[i], headings[i], descs[i])
            entryList.add(savedEntry)
        }

        val adapter = PromptAdapter(entryList)
        promptRecyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : PromptAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@EntryListActivity, MainActivity::class.java)
                intent.putExtra("EXTRA_STRING", entryIds[position])
                startActivity(intent)
            }

        })
        //adapter.notifyDataSetChanged()
    }

    private fun addDrawableImage(context: Context, drawableName: String){
        val fileId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)

        if (fileId != 0)
            imageIds.add(fileId)
        else
            imageIds.add(R.drawable.failsafe_img)
    }

    private fun truncateDesc(input: String): String {
        return if(input.length > DESC_MAX)
            input.substring(0, DESC_MAX).trimEnd() + "..."
        else
            input
    }

    private fun testInsert1(db : SQLiteDatabase){
        db.insert("user_entries", null, TestEntries.testValues1)
        db.insert("user_entries", null, TestEntries.testValues2)
        db.insert("user_entries", null, TestEntries.testValues3)
        db.insert("user_entries", null, TestEntries.testValues4)
        db.insert("user_entries", null, TestEntries.testValues5)
        db.insert("user_entries", null, TestEntries.testValues6)
        db.insert("user_entries", null, TestEntries.testValues7)
        db.insert("user_entries", null, TestEntries.testValues8)
    }
}