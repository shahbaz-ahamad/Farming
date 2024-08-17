package com.shahbaz.farming.util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings

fun showDialogue(
    context: Context,
    title: String,
    message: String,
    positiveButton: String,
    negativeButton: String,
    onClick: () -> Unit
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButton) { dialog, _ ->
            onClick()

        }
        .setNegativeButton(negativeButton) { dialog, _ ->
            dialog.dismiss()
            // Close the app if user cancels
        }
        .setCancelable(false)
        .show()
}


fun progressDialgoue(
    progressDialog: ProgressDialog,
    title: String,
    message: String,
){
    progressDialog.setTitle(title)
    progressDialog.setMessage(message)
    progressDialog.setCancelable(false)
    progressDialog.show()
}