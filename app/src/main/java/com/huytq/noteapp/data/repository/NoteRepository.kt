package com.huytq.noteapp.data.repository

import com.huytq.noteapp.data.model.Note

interface NoteRepository {

    suspend fun insertNewNote(newNote: Note)

    suspend fun getAllNote(): List<Note>

    suspend fun getDetailNote(selectedNoteId:Int): Note?

    suspend fun updateNote(modifiedNote: Note)

    suspend fun deleteSingleNote(deleteNodeId: Int)

}