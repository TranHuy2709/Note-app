package com.huytq.noteapp.domain

import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.repository.NoteRepository

class AddAndUpdateUseCase(private val noteRepository: NoteRepository) {

    suspend fun getDetailNote(selectedNoteId: Int): Note? {
        return noteRepository.getDetailNote(selectedNoteId)
    }

    suspend fun insertNewNote(newNote: Note){
        noteRepository.insertNewNote(newNote)
    }

    suspend fun updateNote(modifiedNote:Note){
        noteRepository.updateNote(modifiedNote)
    }
}