package ua.sviatkuzbyt.newsnow.ui.elements

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager


class SearchEditText(context: Context?, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatEditText(context!!, attrs) {
    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            val mgr = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            mgr.hideSoftInputFromWindow(this.windowToken, 0)
            // User has pressed Back key. So hide the keyboard
            clearFocus()
        }
        return false
    }
}

