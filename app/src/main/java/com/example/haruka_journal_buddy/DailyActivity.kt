package com.example.haruka_journal_buddy

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

class DailyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchDailyPrompt()
    }

    private fun fetchDailyPrompt(){
        var promptId : String?
        var prompt : String?

        val dbHelper : DatabaseHelper = DatabaseHelper(this)

        val datePst = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))

        FirebaseFirestore.getInstance().collection("daily_prompts")
            .whereEqualTo("month", datePst.month.value)
            .whereEqualTo("day", datePst.dayOfMonth)
            .whereEqualTo("year", datePst.year)
            .get()
            .addOnSuccessListener{ querySnapshot ->
                for (document in querySnapshot) {
                    /*
                    println("Document ID: ${document.id}")
                    println("Data: ${document.data}")
                     */
                    promptId = document.id
                    prompt = document.getString("prompt")

                    val dbPromptId : String? = dbHelper.selectStrFromDb(
                        "element_by_prompt_id", promptId, "prompt_id"
                    )

                    if (dbPromptId == null)
                        raisePrompt(false, promptId, prompt)
                    else
                        raisePrompt(true, promptId, prompt)

                    break // protection against dupe valid dates
                }
            }

            .addOnFailureListener { exception ->
                println("Error fetching documents: $exception")
            }


    }

    private fun raisePrompt(inDb: Boolean, promptId: String?, prompt: String?){

    }
}