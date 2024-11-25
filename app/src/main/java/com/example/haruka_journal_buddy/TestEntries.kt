package com.example.haruka_journal_buddy
import android.content.ContentValues

object TestEntries {
    val testValues1 = ContentValues().apply{
        put("entry_id", 1)
        put("prompt_id", "tst1")
        put("prompt", "What's a good meal you've had recently?")
        put("entry", "this is the entry before I write stuff.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-10-28 11:42:07")
        put("datetime_last_modified", "2024-10-28 11:42:07")
    }

    val testValues2 = ContentValues().apply{
        put("entry_id", 2)
        put("prompt_id", "tst2")
        put("prompt", "Write about a sea creature you like.")
        put("entry", "I really like sharks. I'm going to write a lot of words about sharks because of how much I like them. For example, I love the Ikea shark, BLAHAJ.")
        put("icon_filename", "failsafe_img")
        put("datetime_created", "2024-10-28 11:42:07")
        put("datetime_last_modified", "2024-10-30 1:00:00")
    }
}