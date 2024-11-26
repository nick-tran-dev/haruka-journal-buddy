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
        put("icon_filename", "")
        put("datetime_created", "2024-10-28 11:42:07")
        put("datetime_last_modified", "2024-10-30 1:00:00")
    }

    val testValues3 = ContentValues().apply{
        put("entry_id", 3)
        put("prompt_id", "tst3")
        put("prompt", "Is there someone who you wish would text you right now?")
        put("entry", "I haven't talked with my friend Lucy for a while. Last time I saw her, she was talking about some kinda serious stuff. I hope everything's okay.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }
}