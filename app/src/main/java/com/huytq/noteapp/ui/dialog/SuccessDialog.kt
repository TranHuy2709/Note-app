package com.huytq.noteapp.ui.dialog

import android.view.WindowManager
import com.huytq.noteapp.R
import com.huytq.noteapp.databinding.DialogLoadingBinding
import com.huytq.noteapp.databinding.DialogSuccessBinding
import com.huytq.noteapp.ui.base.BaseDialog

class SuccessDialog(): BaseDialog<DialogSuccessBinding>() {

    constructor(onDismissDialog: ()-> Unit): this(){
        this.onDismissDialog= onDismissDialog
    }

    override val layoutId: Int
        get() = R.layout.dialog_success
    private lateinit var onDismissDialog: () -> Unit
    private var description: String?= null

    fun setDescription(description: String?){
        this.description= description
    }

    override fun initView() {
        if(description != null){
            binding.description.text= description
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}