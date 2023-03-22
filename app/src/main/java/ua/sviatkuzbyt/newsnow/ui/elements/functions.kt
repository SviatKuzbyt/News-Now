//package ua.sviatkuzbyt.newsnow.ui.elements
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.res.Configuration
//import android.content.res.Configuration.UI_MODE_NIGHT_YES
//import android.net.Uri
//import android.view.View
//import android.view.inputmethod.InputMethodManager
//import android.widget.Toast
//import androidx.core.content.ContextCompat
//
//fun hideKeyboardFrom(context: Context, view: View) {
//    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(view.windowToken, 0)
//}
//
//fun shareNews(context: Context, label: String, link: String){
//    try {
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "$label: $link")
//        shareIntent.type = "text/plain"
//        ContextCompat.startActivity(context, shareIntent, null)
//
//    } catch (e: Exception){
//        Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
//    }
//}
//
//fun openNews(context: Context, link: String){
//    try {
//        val urlIntent = Intent(
//            Intent.ACTION_VIEW,
//            Uri.parse(link)
//        )
//        ContextCompat.startActivity(context, urlIntent, null)
//
//    } catch (e: Exception){
//        Toast.makeText(context, "App don't founded for this action", Toast.LENGTH_LONG).show()
//    }
//}
//
//fun Context.isDarkThemeOn(): Boolean {
//    return resources.configuration.uiMode and
//            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
//}
//
