package ua.sviatkuzbyt.newsnow.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.database.DataRepository
import ua.sviatkuzbyt.newsnow.data.database.SaveNewsDataBase
import ua.sviatkuzbyt.newsnow.data.repositories.SearchRepository

class SearchViewModel(application: Application): AndroidViewModel(application) {
    private val _list = mutableListOf<NewsContainer>()
    private val repository: SearchRepository
    private val dataRepository: DataRepository

    val list = MutableLiveData<List<NewsContainer>>(_list)
    val error = MutableLiveData<Boolean>()
    var oldSize = 0

    init {
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        repository = SearchRepository(data)
    }

    fun getNews(q: String){
        viewModelScope.launch(Dispatchers.IO) {
            val news = repository.search(q)
            if (news != null){
                oldSize = _list.size
                _list.clear()
                _list.addAll(news)
                list.postValue(_list)
            } else error.postValue(true)
        }
    }

    fun addSavedNews(item: NewsContainer, updateSaved: Int){
        viewModelScope.launch(Dispatchers.IO){
            dataRepository.addSavedNews(item)
            _list[updateSaved].isSaved = true
        }
    }

    fun removeSavedNews(item: String, updateSaved: Int){
        viewModelScope.launch(Dispatchers.IO){
            dataRepository.removeSavedNews(item)
            _list[updateSaved].isSaved = false
        }
    }
}