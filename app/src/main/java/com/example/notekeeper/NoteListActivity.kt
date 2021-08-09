package com.example.notekeeper

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note_list.*
import kotlinx.android.synthetic.main.content_note_list.*

class NoteListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        setSupportActionBar(findViewById(R.id.toolbar))

        fab.setOnClickListener {
            // Launches activity for editing notes (MainActivity)
            val activityIntent = Intent(this, MainActivity::class.java)
            startActivity(activityIntent)
        }
        // Populates the list view "listNotes"
        listNotes.adapter = ArrayAdapter(this,
                            android.R.layout.simple_list_item_1,
                        DataManager.notes)

        // Launches the NoteActivity and displays the note selected
        listNotes.setOnItemClickListener{ _, _, position, _ ->
            val activityIntent = Intent(this, MainActivity::class.java)
            // passes the data of the note selected to the activity to be launched
            activityIntent.putExtra(NOTE_POSITION, position)
            startActivity(activityIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        (listNotes.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }
}