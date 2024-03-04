package com.huytq.noteapp.domain

import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.repository.NoteRepository

class MainActivityUseCase(private val noteRepository: NoteRepository) {

    suspend fun getAllNote(): List<Note>{
        return noteRepository.getAllNote()
    }

    suspend fun deleteSingleNote(deleteNoteId: Int){
        noteRepository.deleteSingleNote(deleteNoteId)
    }

    suspend fun getDetailNote(updateNoteId: Int): Note?{
        return noteRepository.getDetailNote(updateNoteId)
    }

}