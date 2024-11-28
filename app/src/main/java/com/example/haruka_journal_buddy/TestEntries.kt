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

    val testValues4= ContentValues().apply{
        put("entry_id", 4)
        put("prompt_id", "tst4")
        put("prompt", "What's one thing you wish improved about your life right now?")
        put("entry", "I wish this project was finished lol. Seriously, it's pretty hard.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }

    val testValues5= ContentValues().apply{
        put("entry_id", 5)
        put("prompt_id", "tst5")
        put("prompt", "Do you have a favorite fast food restaurant?")
        put("entry", "In-N-Out for sure. But shoutouts to Raising Canes as the runner up.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }

    val testValues6= ContentValues().apply{
        put("entry_id", 6)
        put("prompt_id", "tst6")
        put("prompt", "Is there a kind of Youtube content you've been into lately?")
        put("entry", "I've been watching a lot of Summoning Salt recently. He does documentaries on video game speedruns, where they try to beat the games as fast as possible.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }

    val testValues7= ContentValues().apply{
        put("entry_id", 7)
        put("prompt_id", "tst7")
        put("prompt", "What's been your go-to snack recently?")
        put("entry", "I've been eating a lot of Pringles recently. Ever since I watched Charlie's tier list on them, I've been really into it. My favorite is either farmhouse cheddar or sweet potato sea salt.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }

    val testValues8= ContentValues().apply{
        put("entry_id", 8)
        put("prompt_id", "tst8")
        put("prompt", "What motivates you to finish mundane tasks?")
        put("entry", "Tbh, it's pretty hard. But some days, I just have this urge to lock in, and I'm really good at doing that stuff on that day.")
        put("icon_filename", "test_image1")
        put("datetime_created", "2024-11-24 11:42:07")
        put("datetime_last_modified", "2024-11-24 1:00:00")
    }
}