package ua.sviatkuzbyt.newsnow.ui.search

import android.app.Application
import android.widget.ProgressBar
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
    private var lastSearch = ""
    private var page = 0
    private val _listHistory = mutableListOf<String>()

    val list = MutableLiveData<List<NewsContainer>>(_list)
    val error = MutableLiveData<Boolean>()
    var oldSizeSearch = 0
    var oldSizeHistory = 0
    val deleteHistory = MutableLiveData<Int>()
    var updating = false
    var loadmore = false
    val emptySearch = MutableLiveData<Boolean>()

    val listHistory = MutableLiveData<List<String>>(_listHistory)

    init {
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        repository = SearchRepository(data)

        viewModelScope.launch(Dispatchers.IO){
            _listHistory.addAll(dataRepository.getHistory())
            oldSizeHistory = _listHistory.size
            listHistory.postValue(_listHistory)
        }

    }

    fun getNews(q: String = lastSearch){
        updating = true
        lastSearch = q
        viewModelScope.launch(Dispatchers.IO) {
            if (!loadmore) page = 0
            dataRepository.addHistory(q)
            page ++
            val news = repository.search(q, page)
            if (news != null){
                if (news.isEmpty()){
                    emptySearch.value = true
                }else{
                    oldSizeSearch = _list.size
                    if (!loadmore) _list.clear()
                    _list.addAll(news)
                    list.postValue(_list)
                }

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

    fun deleteHistory(history: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.deleteHistory(history)
            _listHistory.remove(history)
        }
    }
}