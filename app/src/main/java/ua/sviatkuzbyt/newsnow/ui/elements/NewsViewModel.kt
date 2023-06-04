package ua.sviatkuzbyt.newsnow.ui.elements

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.SharedData

open class NewsViewModel(application: Application): AndroidViewModel(application) {
    val savedNewsRepository = DataBaseRepository(application)

    fun addSavedNews(item: NewsList){
        SharedData.isChangeSaved = true
        viewModelScope.launch(Dispatchers.IO){
            savedNewsRepository.add(item)
        }
    }

    open fun removeSavedNews(link: String){
        SharedData.isChangeSaved = true
        viewModelScope.launch(Dispatchers.IO){
            savedNewsRepository.remove(link)
        }
    }
}