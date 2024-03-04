package com.huytq.noteapp.ui.main.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.huytq.noteapp.R
import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.databinding.ItemNoteBinding
import com.huytq.noteapp.ui.base.BaseViewHolder
import com.huytq.noteapp.utils.Utils

class NoteAdapter(
    private val onNoteClick: (Int) -> Unit,
    private val onEditMode: () -> Unit
) : RecyclerView.Adapter<BaseViewHolder>(), Filterable {

    private val listNotes = mutableListOf<ItemNote>()
    private val filterNotes = mutableListOf<ItemNote>()
    private var editMode = false

    fun getFilterNote(): List<ItemNote>{
        return filterNotes
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAllData(newData: List<Note>) {
        listNotes.clear()
        filterNotes.clear()
        listNotes.addAll(newData.map { note -> noteToItemNote(note) })
        filterNotes.addAll(newData.map { note -> noteToItemNote(note) })
        notifyDataSetChanged()
    }

    private fun noteToItemNote(note: Note): ItemNote {
        return ItemNote().apply {
            this.noteId = note.noteId
            this.noteTitle = note.noteTitle
            this.noteContent = note.noteContent
            this.noteLastModify = note.noteLastModify
        }
    }

    fun updateNote(modifiedNote: Note) {
        val notePosition = filterNotes.indexOfFirst { note -> note.noteId == modifiedNote.noteId }
        if (notePosition != -1) {
            filterNotes[notePosition] = noteToItemNote(modifiedNote)
            notifyItemChanged(notePosition)
        }
    }

    fun removeNote(noteId: Int){
        val deleteNotePosition = filterNotes.indexOfFirst{note-> note.noteId == noteId }
        if(deleteNotePosition != -1){
            val deleteNote= filterNotes[deleteNotePosition]
            listNotes.remove(deleteNote)
            filterNotes.remove(deleteNote)
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun cancelEditMode(){
        editMode= false
        filterNotes.forEach { note-> note.isCheck= false }
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCheckAll(){
        val hasUncheck= filterNotes.find { note -> !note.isCheck }
        if(hasUncheck != null){
            //con item chua check thi check all
            filterNotes.forEach { note -> note.isCheck= true }
        }else{
            //cac item check het, bo check tat ca
            filterNotes.forEach { note -> note.isCheck= false }
        }
        notifyDataSetChanged()
    }

    fun getListCheckedNoteId(): List<Int>{
        return filterNotes.filter { note-> note.isCheck && note.noteId != null }.map { it.noteId!! }.toList()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterListNotes = if (p0.isNullOrBlank()) {
                    listNotes
                } else {
                    listNotes.filter { note ->
                        note.noteTitle?.lowercase()?.contains(p0.toString().lowercase()) == true
                                || note.noteContent?.lowercase()
                            ?.contains(p0.toString().lowercase()) == true
                    }
                }
                filterNotes.clear()
                filterNotes.addAll(filterListNotes)
                return FilterResults()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterNotes.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        BaseViewHolder(binding.root) {
        override fun onBind(position: Int) {
            binding.title.text = filterNotes[position].noteTitle
            binding.lastTimeModify.text =
                Utils.convertTimeStampToNoteTime(filterNotes[position].noteLastModify ?: System.currentTimeMillis())
            binding.content.text = filterNotes[position].noteContent
            binding.divineLine.visibility = if (position == filterNotes.lastIndex) {
                View.GONE
            } else {
                View.VISIBLE
            }
            binding.noteLayout.setOnClickListener {
                if (!editMode) {
                    filterNotes[position].noteId?.let { noteId ->
                        onNoteClick(noteId)
                    }
                }else{
                    filterNotes[position].isCheck = !filterNotes[position].isCheck
                    onNoteClick(0)
                    notifyItemChanged(position)
                }
            }
            binding.noteLayout.setOnLongClickListener {
                if (!editMode) {
                    editMode = true
                    onEditMode()
                    notifyDataSetChanged()
                    return@setOnLongClickListener true
                } else {
                    return@setOnLongClickListener false
                }
            }
            binding.checkBox.visibility = if (editMode) {
                View.VISIBLE
            } else {
                View.GONE
            }
            val checkBoxResId = if (filterNotes[position].isCheck) {
                R.drawable.ic_check_box_checked
            } else {
                R.drawable.ic_check_box_uncheck
            }
            binding.checkBox.setImageResource(checkBoxResId)
        }
    }
}