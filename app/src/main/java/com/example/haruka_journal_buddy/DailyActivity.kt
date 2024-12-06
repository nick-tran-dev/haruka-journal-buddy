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
        val promptId : String


        val datePst = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"))

        //val db = FirebaseFirestore.getInstance()

        FirebaseFirestore.getInstance().collection("daily_prompts")
            .whereEqualTo("month", datePst.month.value)
            .whereEqualTo("day", datePst.dayOfMonth)
            .whereEqualTo("year", datePst.year)
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
}