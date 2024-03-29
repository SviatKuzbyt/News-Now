package ua.sviatkuzbyt.newsnow.ui.elements

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent

class SearchEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) clearFocus()
        return false
    }
}