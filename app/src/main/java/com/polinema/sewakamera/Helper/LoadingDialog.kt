package com.polinema.sewakamera.Helper

import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import com.polinema.sewakamera.R

class LoadingDialog(private val activity: Activity){
    private var dialog: AlertDialog? = null

    fun startLoadingDialog() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(false)
        dialog = builder.create()
        dialog?.show()
        
    }


    fun dismissDialog() {
        dialog!!.dismiss()
    }
}