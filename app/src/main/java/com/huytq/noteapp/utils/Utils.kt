package com.huytq.noteapp.utils

import java.text.SimpleDateFormat

object Utils {

    fun convertTimeStampToNoteTime(timestamp: Long): String?{
        val sdf = SimpleDateFormat("HH:mm dd/MM/yyyy")
        try {
            return sdf.format(timestamp)
        }catch (ex: IllegalArgumentException){
            return  null
        }
    }
}