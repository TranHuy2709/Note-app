package com.huytq.noteapp.data.repositoryimpl

import com.huytq.noteapp.data.local.NoteDbHelper
import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.repository.NoteRepository

class NoteRepositoryImpl(private val noteDbHelper: NoteDbHelper): NoteRepository {

    override suspend fun insertNewNote(newNote: Note) {
        noteDbHelper.insertNewNote(newNote)
    }

    override suspend fun getAllNote(): List<Note> {
        return noteDbHelper.getAllNote()
    }

    override suspend fun getDetailNote(selectedNoteId: Int): Note? {
        return noteDbHelper.getDetailNote(selectedNoteId)
    }

    override suspend fun updateNote(modifiedNote: Note) {
        noteDbHelper.updateNote(modifiedNote)
    }

    override suspend fun deleteSingleNote(deleteNodeId: Int) {
        noteDbHelper.deleteSingleNote(deleteNodeId)
    }

}