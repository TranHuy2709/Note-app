package com.huytq.noteapp.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.huytq.noteapp.R
import com.huytq.noteapp.data.utils.DataResponse
import com.huytq.noteapp.data.utils.LoadDataStatus
import com.huytq.noteapp.databinding.ActivityMainBinding
import com.huytq.noteapp.ui.addandupdate.AddAndUpdateNoteActivity
import com.huytq.noteapp.ui.base.BaseActivity
import com.huytq.noteapp.ui.main.adapter.NoteAdapter

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutId: Int
        get() = R.layout.activity_main

    private val mainViewModel: MainViewModel by viewModels()
    private val noteAdapter: NoteAdapter by lazy {
        NoteAdapter(
            onNoteClick = {
                if(binding.editMode){
                    val numberOdCheckedNote= noteAdapter.getListCheckedNoteId().size
                    binding.numberItemSelected.text= String.format(getString(R.string.main_number_note_selected_pattern), numberOdCheckedNote)
                }else{
                    gotoAddAndUpdateActivity(it)
                }
            },
            onEditMode = {
                binding.editMode= true
                val numberOdCheckedNote= noteAdapter.getListCheckedNoteId().size
                binding.numberItemSelected.text= String.format(getString(R.string.main_number_note_selected_pattern), numberOdCheckedNote)
            }
        )
    }
    //khong cho phep user vua scroll vua search -> crash app
    private var userCanSearch= true
    //launcher start activity for result
    private val startActivityForResultLauncher= registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == AddAndUpdateNoteActivity.RESULT_CODE_INSERT){
            mainViewModel.getAllNote()
        }
        if(result.resultCode == AddAndUpdateNoteActivity.RESULT_CODE_UPDATE){
            val detailNoteId= result.data?.getIntExtra(AddAndUpdateNoteActivity.ID_NOTE_UPDATE, INVALID_NOTE_ID)
            if(detailNoteId != null && detailNoteId != INVALID_NOTE_ID){
                mainViewModel.getDetailNote(detailNoteId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getAllNote()
    }

    override fun observerData(){
        mainViewModel.getListNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    hideLoadingDialog()
                    val data= (it as DataResponse.DataSuccessResponse).body
                    noteAdapter.updateAllData(data)
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
        mainViewModel.deleteNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    val value= (it as DataResponse.DataSuccessResponse).body
                    if(value.second){
                        //neu la id cua note cuoi cung
                        hideLoadingDialog()
                    }
                    noteAdapter.removeNote(value.first)
                    if(noteAdapter.getFilterNote().isEmpty()){
                        //neu da xoa het note trong list filter, quay ve man tat ca ghi chu
                        noteAdapter.filter.filter("")
                        noteAdapter.cancelEditMode()
                        binding.edtSearch.setText("")
                        binding.editMode= false
                    }
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
        mainViewModel.getDetailNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    hideLoadingDialog()
                    val modifiedNote= (it as DataResponse.DataSuccessResponse).body
                    modifiedNote?.let {
                        noteAdapter.updateNote(modifiedNote)
                    }
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
    }

    override fun initView(){
        binding.editMode= false
        binding.edtSearch.addTextChangedListener {
            noteAdapter.filter.filter(it?.toString())
        }
        binding.addNewNote.setOnClickListener {
            gotoAddAndUpdateActivity(INVALID_NOTE_ID)
        }
        binding.noteRecyclerView.adapter= noteAdapter
        binding.noteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                userCanSearch= newState == RecyclerView.SCROLL_STATE_IDLE
            }
        })
        binding.closeEditMode.setOnClickListener {
            binding.editMode= false
            noteAdapter.cancelEditMode()
        }
        binding.deleteNote.setOnClickListener {
            if(noteAdapter.getListCheckedNoteId().isNotEmpty()){
                mainViewModel.deleteNote(noteAdapter.getListCheckedNoteId())
            }
        }
        binding.checkAll.setOnClickListener {
            noteAdapter.setCheckAll()
        }
    }

    private fun gotoAddAndUpdateActivity(noteId: Int){
        val intent= Intent(this, AddAndUpdateNoteActivity::class.java)
        val bundle= Bundle().apply {
            putInt(DETAILED_NOTE_ID, noteId)
        }
        intent.putExtras(bundle)
        startActivityForResultLauncher.launch(intent)
    }

    companion object{
        const val DETAILED_NOTE_ID= "DETAILED_NOTE_ID"
        const val INVALID_NOTE_ID = -1
    }

}