package ua.sviatkuzbyt.newsnow.ui.saved

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.changeSaves
import ua.sviatkuzbyt.newsnow.data.database.SavedRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.elements.NewsViewModel

class SavedViewModel(application: Application) : NewsViewModel(application){
    val savedList = MutableLiveData<MutableList<NewsList>>()
    private var _savedList = mutableListOf<NewsList>()
    private val repository = SavedRepository(savedNewsRepository)
    var allUpdate = true
    var deleteItem = 0

    fun loadSavedList(){
        if(changeSaves){
            viewModelScope.launch(Dispatchers.IO){
                allUpdate = true

                _savedList = repository.getList()
                savedList.postValue(_savedList)
            }
        }
    }

    override fun removeSavedNews(link: String){
        viewModelScope.launch(Dispatchers.IO){
            allUpdate = false
            deleteItem = _savedList.indexOfFirst { it.link == link }
            _savedList.removeAt(deleteItem)
            savedNewsRepository.remove(link)
            savedList.postValue(_savedList)
        }
    }
}