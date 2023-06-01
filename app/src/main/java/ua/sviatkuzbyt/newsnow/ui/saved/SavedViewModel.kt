package ua.sviatkuzbyt.newsnow.ui.saved

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.database.SavedRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.elements.NewsViewModel

class SavedViewModel(application: Application) : NewsViewModel(application){
    val savedList = MutableLiveData<MutableList<NewsList>>()
    private val repository = SavedRepository(savedNewsRepository)

    init {
        loadSavedList()
    }

    fun loadSavedList(){
        viewModelScope.launch(Dispatchers.IO){
            savedList.postValue(repository.getList())
        }
    }
}