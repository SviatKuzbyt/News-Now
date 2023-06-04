package ua.sviatkuzbyt.newsnow.ui.elements

import android.content.Context
import android.widget.Toast

fun makeToast(context: Context?, text: String){
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}