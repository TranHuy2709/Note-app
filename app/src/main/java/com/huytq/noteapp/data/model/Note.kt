package com.huytq.noteapp.data.model

import com.huytq.noteapp.ui.main.adapter.ItemNote

open class Note(
    var noteId: Int? = 0,
    var noteTitle: String? = "",
    var noteContent: String? = "",
    var noteLastModify: Long? = 0
){

    fun toItemNote(): ItemNote{
        return ItemNote().apply {
            this.noteId = noteId
            this.noteTitle= noteTitle
        }
    }
}