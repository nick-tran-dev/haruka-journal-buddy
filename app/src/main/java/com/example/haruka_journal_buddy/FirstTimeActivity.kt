package com.example.haruka_journal_buddy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerAdapter(
    private val pageTitles: List<String>
    ,private val pageDescs: List<String>
    ,private val context: Context
) : RecyclerView.Adapter<ViewPagerAdapter.PageViewHolder>() {

    // ViewHolder class for managing views
    class PageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.first_time_main_text)
        val desc: TextView = itemView.findViewById(R.id.first_time_desc)
        val nameEntry: EditText = itemView.findViewById(R.id.first_time_name_entry)
        val beginButton: Button = itemView.findViewById(R.id.first_time_begin_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.first_time_page, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.title.text = pageTitles[position]
        holder.desc.text = pageDescs[position]

        if (position == pageTitles.size - 1){
            holder.nameEntry.visibility = View.VISIBLE
            holder.beginButton.visibility = View.VISIBLE

            holder.beginButton.setOnClickListener{
                if (holder.nameEntry.text.isNotEmpty()){
                    val dbHelper = DatabaseHelper(context)
                    dbHelper.updateSetting("user_name", holder.nameEntry.text.toString())
                    dbHelper.close()

                    val intent = Intent(context, EntryListActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                }
            }
        }
        else{
            holder.nameEntry.visibility = View.INVISIBLE
            holder.beginButton.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int = pageTitles.size
}

class FirstTimeActivity : AppCompatActivity() {
    private lateinit var dbHelper : DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_first_time)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        val newUser : Boolean = dbHelper.selectStrFromDb(
            "setting_by_id", "done_first_time", "value"
        ) == "0"

        if (newUser)
            firstTimeSetup()
        else
            startActivity(Intent(this, EntryListActivity::class.java))

        dbHelper.close()
    }

    private fun firstTimeSetup(){
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        val titles = listOf(
            "Welcome to Haruka"
            ,"Journal with prompts, not dates."
            ,"Here's some prompt examples:"
            ,"Let's get started."
        )

        val descs = listOf(
            "Your new journal buddy."
            ,"You'll be given a new prompt every day. Each prompt is a new journal entry for you to write in."
            ,"Write about a nice meal you've had recently.\n\n"
                +"What are you most looking forward to this weekend?\n\n"
                +"What's one thing that mildly annoys you?"
            ,""
        )

        val adapter = ViewPagerAdapter(titles, descs, this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}