package ua.sviatkuzbyt.newsnow.ui.elements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList

open class NewsViewModel(application: Application): AndroidViewModel(application) {
    val savedNewsRepository = DataBaseRepository(application)
    var test = ""

    fun addSavedNews(item: NewsList){
        viewModelScope.launch(Dispatchers.IO){
            savedNewsRepository.addSavedNews(item)
        }
    }

    open fun removeSavedNews(link: String){
        viewModelScope.launch(Dispatchers.IO){
            savedNewsRepository.remove(link)
        }
    }
}
