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
    private var _savedList = mutableListOf<NewsList>()
    private val repository = SavedRepository(savedNewsRepository)
    var deleteItem = -1

    init {
        loadSavedList()
    }

    fun loadSavedList(){
        viewModelScope.launch(Dispatchers.IO){
            deleteItem = -1
            _savedList = repository.getList()
            savedList.postValue(_savedList)
        }
    }

    override fun removeSavedNews(link: String){
        viewModelScope.launch(Dispatchers.IO){
            deleteItem = _savedList.indexOfFirst { it.link == link }
            _savedList.removeAt(deleteItem)
            savedNewsRepository.remove(link)
            savedList.postValue(_savedList)
        }
    }
}