package com.huytq.noteapp.ui.dialog

import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.huytq.noteapp.R
import com.huytq.noteapp.databinding.DialogLoadingBinding
import com.huytq.noteapp.ui.base.BaseDialog

class LoadingDialog: BaseDialog<DialogLoadingBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_loading

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
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT)
    }
}