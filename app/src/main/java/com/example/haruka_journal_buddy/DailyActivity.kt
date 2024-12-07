package com.example.haruka_journal_buddy

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyActivity : AppCompatActivity() {
    private lateinit var dbHelper : DatabaseHelper
    private lateinit var userDb: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.error_back_button).setOnClickListener{
            startActivity(Intent(this, EntryListActivity::class.java))
        }



        //fetchDailyPrompt()
    }

    override fun onResume(){
        super.onResume()
        dbHelper = DatabaseHelper(this)
        userDb = dbHelper.writableDatabase
        fetchDailyPrompt()
    }

    override fun onDestroy(){
        super.onDestroy()
        dbHelper.close()
        userDb.close()
    }


    private fun fetchDailyPrompt(){
        //val promptId : String?
        //val prompt : String?

        //val dbHelper : DatabaseHelper = DatabaseHelper(this)

        //val datePst = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))
        val datePst = LocalDate.of(2024, 12, 5)

        FirebaseFirestore.getInstance().collection("daily_prompts")
            .whereEqualTo("month", datePst.month.value)
            .whereEqualTo("day", datePst.dayOfMonth)
            .whereEqualTo("year", datePst.year)
            .get()
            .addOnSuccessListener{ querySnapshot ->
                if (querySnapshot.isEmpty)
                    firebaseFetchError()

                for (document in querySnapshot) {
                    val promptId = document.id
                    val prompt = document.getString("prompt")

                    Log.d("prompt_id and exists", promptId + " " + dbHelper.promptExists(promptId).toString())

                    raisePrompt(dbHelper.promptExists(promptId), promptId, prompt)

                    break // protection against dupe valid dates
                }
            }

            .addOnFailureListener { exception ->
                println("Error fetching documents: $exception")
                firebaseFetchError()
            }
    }

    private fun raisePrompt(inDb: Boolean, promptId: String?, prompt: String?){
        val promptTextView = findViewById<TextView>(R.id.daily_prompt)
        val entryButton = findViewById<Button>(R.id.entry_button)
        val returnButton = findViewById<Button>(R.id.return_button)
        val divider = findViewById<View>(R.id.divider)

        promptTextView.text = prompt
        findViewById<TextView>(R.id.error_back_button).visibility = View.INVISIBLE

        listOf(entryButton, returnButton, divider).forEach{
            it.visibility = View.VISIBLE
        }

        returnButton.setOnClickListener{ startActivity(Intent(this, EntryListActivity::class.java)) }

        //val dbHelper : DatabaseHelper = DatabaseHelper(this)
        //val userDb = dbHelper.writableDatabase

        var entryId : Int?

        if (inDb){
            entryButton.text = "Resume"
            entryId = dbHelper.selectStrFromDb(
                "entry_by_prompt_id", promptId, "entry_id"
            )?.toInt()

            entryButton.setOnClickListener{
                val intent = Intent(this@DailyActivity, MainActivity::class.java)
                intent.putExtra("EXTRA_STRING", entryId.toString())
                startActivity(intent)
            }
        }

        else{
            entryId = dbHelper.selectIntFromDb("top_entry", null, "max_entry_id")

            if (entryId == null)
                entryId = 1
            else
                entryId++

            entryButton.setOnClickListener{
                userDb.insert(
                    "user_entries"
                    ,null
                    ,ContentValues().apply{
                        put("entry_id", entryId)
                        put("prompt_id", promptId)
                        put("prompt", prompt)
                        put("entry", "")
                        put("icon_filename", "test_image1")
                        put("datetime_created", dbHelper.now())
                        put("datetime_last_modified", dbHelper.now())
                    }
                )

                val intent = Intent(this@DailyActivity, MainActivity::class.java)
                intent.putExtra("EXTRA_STRING", entryId.toString())
                startActivity(intent)
            }
        }


        //userDb.close()
        //dbHelper.close()
    }

    private fun firebaseFetchError(){
        findViewById<TextView>(R.id.daily_prompt).text = "Error"
        findViewById<TextView>(R.id.error_textview).visibility = View.VISIBLE
    }
}