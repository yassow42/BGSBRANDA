package com.creativeoffice.bgsbranda

import android.app.Dialog
import android.content.Context
import android.graphics.Color


object LoadingDialog {

    fun startDialog(context: Context): Dialog? {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
          // it.window?.setBackgroundDrawableResource(R.color.siyah)
            it.setContentView(R.layout.proggres_dialog)
            it.setCancelable(true)
            it.setCanceledOnTouchOutside(true)
            return it
        }
    }
 fun startDialog2(context: Context): Dialog? {
        val progressDialog = Dialog(context)

        progressDialog.let {
            it.show()
          // it.window?.setBackgroundDrawableResource(R.color.siyah)
            it.setContentView(R.layout.proggres_dialog2)
            it.setCancelable(false)
            it.setCanceledOnTouchOutside(false)
            return it
        }
    }



}