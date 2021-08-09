package com.example.notekeeper

import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class DataManagerTest {

    @After // Runs after each test is run
    fun setUp() {
        DataManager.notes.clear()
        DataManager.initializeNotes()
    }

    @Test
    fun addNote() {
        val course = DataManager.courses["android_async"]!!
        val noteTitle = "Test note"
        val noteText = "Test note text"
        val index = DataManager.addNote(course, noteTitle, noteText)
        val note = DataManager.notes[index]

        assertEquals(course, note.course)
        assertEquals(noteTitle, note.title)
        assertEquals(noteText, note.text)
    }

    @Test
    fun findSimilarNotes() {
        val course = DataManager.courses["android_async"]!!
        val noteTitle = "Test note"
        val noteText1 = "Test note text"
        val noteText2 = "Test note text two"

        val index1 = DataManager.addNote(course, noteTitle, noteText1)
        val index2 = DataManager.addNote(course, noteTitle, noteText2)

        val note1 = DataManager.findNote(course, noteTitle, noteText1)
        val foundIndex1 = DataManager.notes.indexOf(note1)
        assertEquals(index1, foundIndex1)

        val note2 = DataManager.findNote(course, noteTitle, noteText2)
        val foundIndex2 = DataManager.notes.indexOf(note2)
        assertEquals(index2, foundIndex2)
    }
}