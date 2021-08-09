package com.example.notekeeper

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val tag = this::class.simpleName
    private var notePosition = POSITION_NOT_SET

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val adapterCourses = ArrayAdapter(this,
                                android.R.layout.simple_spinner_item,
                                DataManager.courses.values.toList())
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Populates the spinner
        spinnerCourses.adapter = adapterCourses

        // Assigns the position of the of the displayed note
        notePosition = savedInstanceState?.getInt(NOTE_POSITION, POSITION_NOT_SET) ?:
                intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET)

        if (notePosition != POSITION_NOT_SET)
            displayNote()
        else {
            createNewNote()
        }
        Log.d(tag, "onCreate")
    }

    private fun createNewNote() {
        DataManager.notes.add(NoteInfo())
        notePosition = DataManager.notes.lastIndex
    }

    // saves the state of an activity when it's to get destroyed
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(NOTE_POSITION, notePosition)
    }

    private fun displayNote() {
        if (notePosition < 1)
            showMessage("This is the first note")
        else if (notePosition >= DataManager.notes.lastIndex)
            showMessage("No more notes")

        Log.i(tag, "Displaying note for position $notePosition")
        val note = DataManager.notes[notePosition]
        // Sets the note to be displayed; tile and note
        textNoteTitle.setText(note.title)
        textNoteText.setText(note.text)

        val coursePosition = DataManager.courses.values.indexOf(note.course)
        // sets the course to be displayed in the spinner
        spinnerCourses.setSelection(coursePosition)
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
            R.id.action_previous -> {
                movePrevious()
                true
            }
            R.id.action_next -> {
                moveNext()
                true
            }
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(textNoteTitle, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun movePrevious() {
        --notePosition
        displayNote()
        invalidateOptionsMenu() // calls onPrepareOptionsMenu
    }

    private fun moveNext() {
        ++notePosition
        displayNote()
        invalidateOptionsMenu() // calls onPrepareOptionsMenu
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        // checks if the note displayed is the first, change the icon and disable it
        if (notePosition <= 0) {
            val menuItem = menu?.findItem(R.id.action_previous)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }
        }
        // checks if the note displayed is the last, change the icon and disable it
        if (notePosition >= DataManager.notes.lastIndex) {
            val menuItem = menu?.findItem(R.id.action_next)
            if (menuItem != null) {
                menuItem.icon = getDrawable(R.drawable.ic_baseline_block_24)
                menuItem.isEnabled = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onPause() {
        super.onPause()
        saveNote()
        Log.d(tag, "onPause")
    }

    private fun saveNote() {
        val note = DataManager.notes[notePosition]
        // save changes to existing note when leaving the activity
        note.title = textNoteTitle.text.toString()
        note.text = textNoteText.text.toString()
        note.course = spinnerCourses.selectedItem as CourseInfo
        Log.i(tag, "Changes saved")
    }
}