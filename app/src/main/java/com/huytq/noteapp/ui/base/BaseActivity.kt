package com.huytq.noteapp.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.huytq.noteapp.ui.dialog.LoadingDialog

abstract class BaseActivity<V: ViewDataBinding> : AppCompatActivity(){

    protected lateinit var binding: V
    protected abstract val layoutId: Int
    //check trang thai show loading, tranh crash app khi double click
    private var showLoading= false
    private val loadingDialog: LoadingDialog by lazy {
        LoadingDialog()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this, layoutId)
        observerData()
        initView()
    }

    fun showLoadingDialog(description: String?= null){
        if(!showLoading){
            showLoading= true
            loadingDialog.setDescription(description)
            loadingDialog.show(supportFragmentManager, "Loading")
        }
    }

    fun hideLoadingDialog(){
        if(showLoading){
            loadingDialog.dismiss()
            showLoading= false
        }
    }

    abstract fun observerData()

    abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}