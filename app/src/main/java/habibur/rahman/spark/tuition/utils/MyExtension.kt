package habibur.rahman.spark.tuition.utils

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import habibur.rahman.spark.tuition.R

object MyExtension {


    fun Context.shortMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun Context.longMessage(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    fun Context.showDialog(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(resources.getString(R.string.ok)
        ) { p0, p1 ->
            p0?.let {
                it.dismiss()
            }
        }
        val alertDialog: AlertDialog=builder.create()
        alertDialog.show()
    }








}