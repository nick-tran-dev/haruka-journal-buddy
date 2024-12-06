package com.example.haruka_journal_buddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import java.time.ZoneId
import java.time.ZonedDateTime

class EntryListActivity : AppCompatActivity() {
    private lateinit var promptRecyclerView: RecyclerView

    private lateinit var entryList: ArrayList<SavedEntry>
    lateinit var entryIds: MutableList<String>
    lateinit var imageIds: MutableList<Int>
    lateinit var headings: MutableList<String>
    lateinit var descs: MutableList<String>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entry_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        val dbHelper : DatabaseHelper = DatabaseHelper(this)
        val userDb = dbHelper.writableDatabase

        val menuButton: MaterialButton = findViewById(R.id.menu_button)
        val overlay: View = findViewById(R.id.entry_list_overlay)
        val menuDaily: Button = findViewById(R.id.entry_menu_daily)
        val menuPrompts: Button = findViewById(R.id.entry_menu_prompts)
        val menuSettings: Button = findViewById(R.id.entry_menu_settings)

        var menuVisible = false

        menuButton.setOnClickListener {
            //startActivity(Intent(this, MenuActivity::class.java))

            if (!menuVisible){
                menuVisible = true

                listOf(overlay, menuDaily, menuPrompts, menuSettings).forEach{
                    it.visibility = View.VISIBLE
                }

                overlay.setOnTouchListener { _, _ -> true } // disables recyclerview clicks

                menuButton.icon = null
                menuButton.text = "Back"
            }

            else{
                menuVisible = false

                listOf(overlay, menuDaily, menuPrompts, menuSettings).forEach{
                    it.visibility = View.INVISIBLE
                }

                overlay.setOnTouchListener(null) // enables recyclerview clicks

                menuButton.icon = ContextCompat.getDrawable(this, R.drawable.baseline_menu_24)
                menuButton.text = ""
            }
        }

        menuSettings.setOnClickListener{ startActivity(Intent(this, SettingsActivity::class.java)) }
        menuDaily.setOnClickListener{ startActivity(Intent(this, DailyActivity::class.java)) }


        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
        dbHelper.WIPEDATABASE()
        testInsert1(userDb)
        /*
        *
        * */

        refreshRecycler()

        //fireTest()
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

        val dbHelper : DatabaseHelper = DatabaseHelper(this)
        val userDb = dbHelper.writableDatabase
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

        val hello: TextView = findViewById(R.id.prompt_list_hello)
        hello.text = "Hi " + dbHelper.selectStrFromDb("setting_by_id", "user_name", "value")

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

    private fun fireTest(){
        val db = FirebaseFirestore.getInstance()

        db.collection("daily_prompts")
            .whereEqualTo("month", 12)
            .whereEqualTo("day", 5)
            .whereEqualTo("year", 2024)
            .get()
            .addOnSuccessListener{ querySnapshot ->
                for (document in querySnapshot) {
                    println("Document ID: ${document.id}")
                    println("Data: ${document.data}")
                }

            }
            .addOnFailureListener { exception ->
                println("Error fetching documents: $exception")
            }
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