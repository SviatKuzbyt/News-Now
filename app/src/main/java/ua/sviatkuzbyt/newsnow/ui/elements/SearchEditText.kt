package ua.sviatkuzbyt.newsnow.ui.elements

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import ua.sviatkuzbyt.newsnow.ui.search.SearchFragment


class SearchEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // User has pressed Back key. So hide the keyboard
            clearFocus()
        }
        return false
    }
}

