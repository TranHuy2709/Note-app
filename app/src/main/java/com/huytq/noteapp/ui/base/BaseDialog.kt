package com.huytq.noteapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.huytq.noteapp.R


abstract class BaseDialog<V: ViewDataBinding>: DialogFragment() {

    protected lateinit var binding: V
    protected abstract val layoutId: Int
    protected var curOwnerId:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView()
        return binding.root
    }

    fun setDialogCurOwnerId(ownerId:Int){
        this.curOwnerId= ownerId
    }

    protected abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

}