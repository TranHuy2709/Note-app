package com.huytq.noteapp.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.huytq.noteapp.data.model.Note

class NoteDbHelper(context: Context): SQLiteOpenHelper(context, NOTE_DATABASE_NAME, null, NOTE_DATABASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(NoteEntry.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(NoteEntry.SQL_DELETE_ENTRIES)
        onCreate(p0)
    }

    fun insertNewNote(newNote: Note): Int{
        val db= this.writableDatabase

        val values = ContentValues().apply {
            put(NoteEntry.COLUMN_NAME_TITLE, newNote.noteTitle)
            put(NoteEntry.COLUMN_NAME_CONTENT, newNote.noteContent)
            put(NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY, newNote.noteLastModify)
        }

        val id= db.insert(NoteEntry.TABLE_NAME, null, values)
        return  id.toInt()
    }

    fun getAllNote(): List<Note>{
        val db= this.readableDatabase

        val projection = arrayOf(BaseColumns._ID, NoteEntry.COLUMN_NAME_TITLE,
            NoteEntry.COLUMN_NAME_CONTENT, NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY)

        val sortOrder= "${NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY} DESC"

        val cursor = db.query(NoteEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder)
        val notes= mutableListOf<Note>()

        while (cursor.moveToNext()){
            val noteId= cursor.getIntOrNull(cursor.getColumnIndex(BaseColumns._ID))
            if(noteId != null){
                val noteTitle= cursor.getStringOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE))
                val noteContent= cursor.getStringOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_CONTENT))
                val noteLastTimeModify = cursor.getLongOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY))
                notes.add(Note(
                    noteId= noteId,
                    noteTitle= noteTitle,
                    noteContent= noteContent,
                    noteLastModify = noteLastTimeModify
                ))
            }
        }

        cursor.close()
        return notes.toList()
    }

    fun getDetailNote(selectNoteId: Int): Note?{
        val db= this.readableDatabase

        // Filter results WHERE "ID" = 'selectNoteId'
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$selectNoteId")

        val projection = arrayOf(BaseColumns._ID, NoteEntry.COLUMN_NAME_TITLE,
            NoteEntry.COLUMN_NAME_CONTENT, NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY)

        val sortOrder= "${NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY} DESC"

        val cursor = db.query(NoteEntry.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder)
        var note: Note?= null

        if(cursor.moveToNext()){
            val noteId= cursor.getIntOrNull(cursor.getColumnIndex(BaseColumns._ID))
            if(noteId != null){
                val noteTitle= cursor.getStringOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_TITLE))
                val noteContent= cursor.getStringOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_CONTENT))
                val noteLastTimeModify = cursor.getLongOrNull(cursor.getColumnIndex(NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY))
                note = Note(
                    noteId= noteId,
                    noteTitle= noteTitle,
                    noteContent= noteContent,
                    noteLastModify = noteLastTimeModify
                )
            }
        }

        cursor.close()
        return note
    }

    fun updateNote(modifiedNote: Note): Int {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(NoteEntry.COLUMN_NAME_TITLE, modifiedNote.noteTitle)
            put(NoteEntry.COLUMN_NAME_CONTENT, modifiedNote.noteContent)
            put(NoteEntry.COLUMN_NAME_LAST_TIME_MODIFY, modifiedNote.noteLastModify)
        }

        // Which row to update, based on the id
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("${modifiedNote.noteId}")

        return db.update(NoteEntry.TABLE_NAME, values, selection, selectionArgs)
    }

    fun deleteSingleNote(deleteNoteId: Int){
        val db= this.writableDatabase

        // Which row to update, based on the noteId
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf("$deleteNoteId")

        db.delete(NoteEntry.TABLE_NAME, selection, selectionArgs)
    }

    companion object{
        const val NOTE_DATABASE_VERSION= 1
        const val NOTE_DATABASE_NAME= "NoteDatabase.db"
    }

    object NoteEntry: BaseColumns{
        const val TABLE_NAME= "note"
        const val COLUMN_NAME_TITLE= "title"
        const val COLUMN_NAME_CONTENT= "content"
        const val COLUMN_NAME_LAST_TIME_MODIFY= "date"

        const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "${COLUMN_NAME_TITLE} TEXT," +
                    "${COLUMN_NAME_CONTENT} TEXT," +
                    "${COLUMN_NAME_LAST_TIME_MODIFY} BIGINT," +
                    "UNIQUE (${BaseColumns._ID}) ON CONFLICT REPLACE)"


        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${TABLE_NAME}"
    }

}