package com.example.haruka_journal_buddy

import android.annotation.SuppressLint
import android.content.ContentValues
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
import java.time.Month
import java.time.ZoneId
import java.time.ZonedDateTime

class EntryListActivity : AppCompatActivity() {
    private lateinit var promptRecyclerView: RecyclerView
    private lateinit var entryList: ArrayList<SavedEntry>
    lateinit var entryIds: MutableList<String>
    lateinit var dates: MutableList<String>
    lateinit var headings: MutableList<String>
    lateinit var descs: MutableList<String>

    private lateinit var dbHelper : DatabaseHelper
    private lateinit var userDb : SQLiteDatabase

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

        val menuButton: MaterialButton = findViewById(R.id.menu_button)
        val overlay: View = findViewById(R.id.entry_list_overlay)
        val menuDaily: Button = findViewById(R.id.entry_menu_daily)
        val menuPrompts: Button = findViewById(R.id.entry_menu_prompts)
        val menuSettings: Button = findViewById(R.id.entry_menu_settings)

        var menuVisible = false

        menuButton.setOnClickListener {
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

        menuPrompts.setOnClickListener{
            getPastPrompts()
            menuButton.performClick()
        }

        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
        /*
        dbHelper = DatabaseHelper(this)
        userDb = dbHelper.writableDatabase

        dbHelper.WIPEDATABASE()
        testInsert1(userDb)

        dbHelper.close()
        userDb.close()

         */
        /*
        *
        * */
    }

    override fun onResume(){
        super.onResume()
        refreshRecycler()
    }

    private fun refreshRecycler(){
        dbHelper = DatabaseHelper(this)
        userDb = dbHelper.writableDatabase

        entryIds = mutableListOf()
        dates = mutableListOf()
        headings = mutableListOf()
        descs = mutableListOf()

        //val dbHelper : DatabaseHelper = DatabaseHelper(this)
        val entries = dbHelper.getSelectResults(dbHelper.entryListSelect)
        //dbHelper.close()

        for (entry in entries){
            entryIds.add(entry["entry_id"].toString())
            dates.add(
                entry["month"].toString() + "/" + entry["day"].toString()
                + "\n" + entry["year"].toString()
            )
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

        dbHelper.close()
        userDb.close()
    }

    private fun getUserEntries(){
        for (i in entryIds.indices){
            val savedEntry = SavedEntry(dates[i], headings[i], descs[i])
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

    private fun getPastPrompts(){
        entryIds = mutableListOf()
        dates = mutableListOf()
        headings = mutableListOf()
        descs = mutableListOf()

        val datePst = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))

        FirebaseFirestore.getInstance().collection("daily_prompts")
            .whereEqualTo("month", datePst.month.value)
            .whereLessThanOrEqualTo("day", datePst.dayOfMonth)
            .whereEqualTo("year", datePst.year)
            .get()
            .addOnSuccessListener{ querySnapshot ->
                for (document in querySnapshot){
                    entryIds.add(document.id)
                    dates.add(
                        document.getLong("month").toString() + "/" + document.getLong("day").toString()
                            + "\n" + document.getLong("year").toString()
                    )
                    headings.add(document.getString("prompt")?: "ERROR")
                    descs.add("") // TODO: omit desc and center header
                }

                promptRecyclerView = findViewById(R.id.prompt_list)
                promptRecyclerView.layoutManager = LinearLayoutManager(this)
                promptRecyclerView.setHasFixedSize(true)

                entryList = arrayListOf<SavedEntry>()

                val hello: TextView = findViewById(R.id.prompt_list_hello)
                hello.text = "From " + datePst.month.name.lowercase().replaceFirstChar { it.uppercase() }

                for (i in entryIds.indices){
                    val savedEntry = SavedEntry(dates[i], headings[i], descs[i])
                    entryList.add(savedEntry)
                }

                val adapter = PromptAdapter(entryList)
                promptRecyclerView.adapter = adapter

                dbHelper = DatabaseHelper(this)
                userDb = dbHelper.writableDatabase

                adapter.setOnItemClickListener(object : PromptAdapter.onItemClickListener{
                    override fun onItemClick(position: Int) {
                        var entryId = dbHelper.selectIntFromDb("top_entry", null, "max_entry_id")
                        entryId = (entryId ?: 0) + 1

                        userDb.insert(
                            "user_entries"
                            ,null
                            , ContentValues().apply{
                                put("entry_id", entryId)
                                put("prompt_id", entryIds[position])
                                put("prompt", headings[position])
                                put("entry", "")
                                put("icon_filename", "test_image1")
                                put("datetime_created", dbHelper.now())
                                put("datetime_last_modified", dbHelper.now())
                            }
                        )

                        println(entryId)

                        val intent = Intent(this@EntryListActivity, MainActivity::class.java)
                        intent.putExtra("EXTRA_STRING", entryId.toString())
                        startActivity(intent)
                    }

                })

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
        db.insert("user_entries", null, TestEntries.testValues9)
        db.insert("user_entries", null, TestEntries.testValues10)
    }
}