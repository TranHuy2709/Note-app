package com.huytq.noteapp.ui.addandupdate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huytq.noteapp.data.local.NoteDbHelper
import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.repositoryimpl.NoteRepositoryImpl
import com.huytq.noteapp.data.utils.DataResponse
import com.huytq.noteapp.data.utils.LoadDataStatus
import com.huytq.noteapp.domain.AddAndUpdateUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddAndUpdateViewModel(application: Application): AndroidViewModel(application) {

    private val addAndUpdateUseCase: AddAndUpdateUseCase by lazy {
        AddAndUpdateUseCase(NoteRepositoryImpl(NoteDbHelper(getApplication())))
    }

    val getDetailNoteResponse= MutableLiveData<DataResponse<Note?>>()

    fun getDetailNote(selectedNoteId: Int)= viewModelScope.launch(Dispatchers.IO) {
        if(getDetailNoteResponse.value?.loadDataStatus == LoadDataStatus.LOADING)return@launch
        getDetailNoteResponse.postValue(DataResponse.DataLoadingResponse())
        val note= addAndUpdateUseCase.getDetailNote(selectedNoteId)
        if(note != null){
            getDetailNoteResponse.postValue(DataResponse.DataSuccessResponse(note))
        }else{
            getDetailNoteResponse.postValue(DataResponse.DataErrorResponse(Throwable(
                GET_DETAIL_NOTE_ERROR)))
        }
    }

    val insertNewNoteResponse= MutableLiveData<DataResponse<Boolean>>()

    fun insertNewNote(newNote: Note)= viewModelScope.launch(Dispatchers.IO) {
        if(insertNewNoteResponse.value?.loadDataStatus == LoadDataStatus.LOADING) return@launch
        insertNewNoteResponse.postValue(DataResponse.DataLoadingResponse())
        addAndUpdateUseCase.insertNewNote(newNote)
        insertNewNoteResponse.postValue(DataResponse.DataSuccessResponse(true))
    }

    val updateNewNoteResponse= MutableLiveData<DataResponse<Boolean>>()

    fun updateNote(modifiedNote: Note)= viewModelScope.launch(Dispatchers.IO) {
        if(updateNewNoteResponse.value?.loadDataStatus == LoadDataStatus.LOADING) return@launch
        updateNewNoteResponse.postValue(DataResponse.DataLoadingResponse())
        addAndUpdateUseCase.updateNote(modifiedNote)
        updateNewNoteResponse.postValue(DataResponse.DataSuccessResponse(true))
    }

    companion object{
        const val GET_DETAIL_NOTE_ERROR= "1"
    }
}