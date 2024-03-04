package com.huytq.noteapp.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.huytq.noteapp.data.local.NoteDbHelper
import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.repositoryimpl.NoteRepositoryImpl
import com.huytq.noteapp.data.utils.DataResponse
import com.huytq.noteapp.data.utils.LoadDataStatus
import com.huytq.noteapp.domain.MainActivityUseCase
import com.huytq.noteapp.ui.addandupdate.AddAndUpdateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    private val mainActivityUserCase: MainActivityUseCase by lazy {
        MainActivityUseCase(NoteRepositoryImpl(NoteDbHelper(getApplication())))
    }

    val getListNoteResponse= MutableLiveData<DataResponse<List<Note>>>()

    fun getAllNote()= viewModelScope.launch(Dispatchers.IO) {
        if(getListNoteResponse.value?.loadDataStatus == LoadDataStatus.LOADING) return@launch
        getListNoteResponse.postValue(DataResponse.DataLoadingResponse())
        val listNote= mainActivityUserCase.getAllNote()
        getListNoteResponse.postValue(DataResponse.DataSuccessResponse(listNote))
    }

    val deleteNoteResponse= MutableLiveData<DataResponse<Pair<Int, Boolean>>>()

    fun deleteNote(listNoteId: List<Int>)= viewModelScope.launch(Dispatchers.IO) {
        val lastId= listNoteId[listNoteId.lastIndex]
        listNoteId.asFlow().onEach{
            deleteNoteResponse.postValue(DataResponse.DataLoadingResponse())
            mainActivityUserCase.deleteSingleNote(it)
        }.collect{
            val isLastId= it == lastId
            deleteNoteResponse.postValue(DataResponse.DataSuccessResponse(Pair(it, isLastId)))
        }
    }

    val getDetailNoteResponse= MutableLiveData<DataResponse<Note?>>()

    fun getDetailNote(updateNoteId: Int)= viewModelScope.launch(Dispatchers.IO) {
        if(getDetailNoteResponse.value?.loadDataStatus == LoadDataStatus.LOADING)return@launch
        getDetailNoteResponse.postValue(DataResponse.DataLoadingResponse())
        val note= mainActivityUserCase.getDetailNote(updateNoteId)
        if(note != null){
            getDetailNoteResponse.postValue(DataResponse.DataSuccessResponse(note))
        }else{
            getDetailNoteResponse.postValue(DataResponse.DataErrorResponse(Throwable(
                AddAndUpdateViewModel.GET_DETAIL_NOTE_ERROR
            )))
        }
    }
}