package com.example.haruka_journal_buddy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class EntryListActivity : AppCompatActivity() {

    private lateinit var promptRecyclerView: RecyclerView
    private lateinit var testList: ArrayList<SavedEntry>
    lateinit var imageIds: Array<Int>
    lateinit var headings: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imageIds = arrayOf(
            R.drawable.test_image1
            ,R.drawable.test_image1
        )

        headings = arrayOf(
            "Write about a nice meal you've had recently."
            ,"Write about a nice meal you've had recently."
        )

        /*
        val testList = arrayListOf("test 1", "test 2", "test 3")

        val recyclerView: RecyclerView = findViewById(R.id.prompt_list)
        val customAdapter = PromptAdapter(testList)
        recyclerView.layoutManager = LinearLayoutManager(this@EntryListActivity)
        recyclerView.adapter = customAdapter
         */

        promptRecyclerView = findViewById(R.id.prompt_list)
        promptRecyclerView.layoutManager = LinearLayoutManager(this)
        promptRecyclerView.setHasFixedSize(true)

        testList = arrayListOf<SavedEntry>()

        getUserEntries()
    }

    private fun getUserEntries(){
        for (i in imageIds.indices){
            val savedEntry = SavedEntry(imageIds[i], headings[i])
            testList.add(savedEntry)
        }
        var adapter = PromptAdapter(testList)
        promptRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}