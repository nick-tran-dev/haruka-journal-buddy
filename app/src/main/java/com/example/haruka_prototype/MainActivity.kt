package com.example.haruka_prototype

import android.content.ContentValues
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.haruka_prototype.databinding.ActivityMainBinding
import java.security.KeyStore.Entry
import android.widget.TextView

import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        /*
        lateinit var dbHelper: EntryDatabaseHelper
        dbHelper = EntryDatabaseHelper(this)
        */

        var dbHelper : EntryDatabaseHelper = EntryDatabaseHelper(this)


        /*
        * INCLUDED FOR TESTING PURPOSES ONLY. DELETE WHEN APPLICABLE
        * */
        dbHelper.WIPEDATABASE()

        val entryDb = dbHelper.writableDatabase
        val testValues = ContentValues().apply{
            put("entry_id", 4)
            put("prompt_id", "tst1")
            put("prompt", "This is a test journal prompt")
            put("entry", "this is me talking about stuff")
            put("datetime", System.currentTimeMillis())
        }

        val testValues2 = ContentValues().apply{
            put("entry_id", 10)
            put("prompt_id", "tst2")
            put("prompt", "My SECOND test prompt")
            put("entry", "today, I had raising canes. it was yummy.")
            put("datetime", System.currentTimeMillis())
        }

        entryDb.insert("user_entries", null, testValues)
        entryDb.insert("user_entries", null, testValues2)

        //setText(dbHelper.getPromptById(1))
        setText(dbHelper.getElementById(4, "entry"))


    }

    private fun setText(inputString: String?) {
        setContentView(R.layout.fragment_first)
        val textViewFirst: TextView = findViewById<TextView>(R.id.textview_first)

        textViewFirst.text = inputString
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}