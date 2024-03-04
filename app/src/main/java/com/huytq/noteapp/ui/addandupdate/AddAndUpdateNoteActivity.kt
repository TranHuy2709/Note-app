package com.huytq.noteapp.ui.addandupdate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import com.huytq.noteapp.R
import com.huytq.noteapp.data.model.Note
import com.huytq.noteapp.data.utils.DataResponse
import com.huytq.noteapp.data.utils.LoadDataStatus
import com.huytq.noteapp.databinding.ActivityAddAndUpdateNoteBinding
import com.huytq.noteapp.ui.base.BaseActivity
import com.huytq.noteapp.ui.dialog.SuccessDialog
import com.huytq.noteapp.ui.main.MainActivity
import com.huytq.noteapp.utils.Constants

class AddAndUpdateNoteActivity: BaseActivity<ActivityAddAndUpdateNoteBinding>(){

    override val layoutId: Int
        get() = R.layout.activity_add_and_update_note

    private val viewModel: AddAndUpdateViewModel by viewModels()
    private var detailNoteId: Int?= null
    private val successDialog : SuccessDialog by lazy {
        SuccessDialog(
            onDismissDialog = {
                finish()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailNoteId= intent.extras?.getInt(MainActivity.DETAILED_NOTE_ID)
        if(detailNoteId != null && detailNoteId != MainActivity.INVALID_NOTE_ID){
            binding.activityTitle.text= getString(R.string.add_note_update_title)
            viewModel.getDetailNote(detailNoteId!!)
        }else{
            binding.activityTitle.text= getString(R.string.add_note_new_title)
        }
    }

    override fun observerData() {
        viewModel.getDetailNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    hideLoadingDialog()
                    val note= (it as DataResponse.DataSuccessResponse).body
                    binding.edtTitle.setText(note?.noteTitle)
                    binding.edtContent.setText(note?.noteContent)
                }
                LoadDataStatus.ERROR->{
                    finish()
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
        viewModel.updateNewNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    hideLoadingDialog()
                    successDialog.setDescription(getString(R.string.add_note_update_success))
                    successDialog.show(supportFragmentManager, "update")
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent= Intent().apply {
                            putExtra(ID_NOTE_UPDATE, detailNoteId)
                        }
                        setResult(RESULT_CODE_UPDATE, intent)
                        finish()
                    }, Constants.DIALOG_DISMISS_DELAY)
                }
                LoadDataStatus.ERROR->{
                    finish()
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
        viewModel.insertNewNoteResponse.observe(this){
            when(it.loadDataStatus){
                LoadDataStatus.LOADING->{
                    showLoadingDialog()
                }
                LoadDataStatus.SUCCESS->{
                    hideLoadingDialog()
                    successDialog.setDescription(getString(R.string.add_note_insert_success))
                    successDialog.show(supportFragmentManager, "insert")
                    setResult(RESULT_CODE_INSERT)
                    Handler(Looper.getMainLooper()).postDelayed({

                        finish()
                    }, Constants.DIALOG_DISMISS_DELAY)
                }
                LoadDataStatus.ERROR->{
                    finish()
                }
                else->{
                    hideLoadingDialog()
                }
            }
        }
    }

    override fun initView() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
        binding.iconDone.setOnClickListener {
            if(binding.edtTitle.text.toString().isBlank() || binding.edtContent.text.toString().isBlank()){
                Toast.makeText(this, getString(R.string.add_note_empty_note_warning), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(detailNoteId != null && detailNoteId != MainActivity.INVALID_NOTE_ID){
                val modifiedNote= Note().apply {
                    this.noteId= detailNoteId
                    this.noteTitle= binding.edtTitle.text.toString()
                    this.noteContent= binding.edtContent.text.toString()
                    this.noteLastModify= System.currentTimeMillis()
                }
                viewModel.updateNote(modifiedNote)
            }else{
                val newNote= Note().apply {
                    this.noteTitle= binding.edtTitle.text.toString()
                    this.noteContent= binding.edtContent.text.toString()
                    this.noteLastModify= System.currentTimeMillis()
                }
                viewModel.insertNewNote(newNote)
            }
        }
    }

    companion object{
        const val RESULT_CODE_UPDATE= 123
        const val ID_NOTE_UPDATE= "UPDATE_NOTE_ID"
        const val RESULT_CODE_INSERT= 456
    }

}