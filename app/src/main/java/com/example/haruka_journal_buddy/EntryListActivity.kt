package com.example.haruka_journal_buddy

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EntryListActivity : AppCompatActivity() {

    private lateinit var promptRecyclerView: RecyclerView
    private lateinit var entryList: ArrayList<SavedEntry>
    lateinit var imageIds: MutableList<Int>
    lateinit var headings: MutableList<String>
    lateinit var descs: MutableList<String>

    private val DESC_MAX = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageIds = mutableListOf()
        headings = mutableListOf()
        descs = mutableListOf()

        val dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)
        //val entryDb = dbHelper.writableDatabase
        val entries = dbHelper.getSelectResults(dbHelper.selectAllModOrdered)

        for (entry in entries){
            //imageIds.add(R.drawable.test_image1)
            addDrawableImage(this, entry["icon_filename"].toString())
            headings.add(entry["prompt"].toString())
            descs.add(
                truncateDesc(entry["entry"].toString())
            )
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
        var adapter = PromptAdapter(entryList)
        promptRecyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : PromptAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@EntryListActivity, "clicked on item $position", Toast.LENGTH_SHORT).show()
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
}