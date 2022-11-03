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
    private val _listHistory = mutableListOf<String>()
    private var lastSearch = ""
    private var page = 0

    private val repository: SearchRepository
    private val dataRepository: DataRepository

    val listSearch = MutableLiveData<List<NewsContainer>>(_list)
    var oldSizeSearch = 0
    val listHistory = MutableLiveData<List<String>>(_listHistory)
    var oldSizeHistory = 0
    val changeHistoryMode = 1

    val deleteHistory = MutableLiveData<Int>()
    var loadModeSearch = 0
    var updatingSearch = false
    val error = MutableLiveData<Int>()

    init {
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        repository = SearchRepository(data)

        viewModelScope.launch(Dispatchers.IO){
            oldSizeHistory = _listHistory.size
            _listHistory.addAll(dataRepository.getHistory())
            oldSizeHistory = _listHistory.size
            listHistory.postValue(_listHistory)
        }

    }

    fun getNews(q: String = lastSearch) = viewModelScope.launch(Dispatchers.IO) {

        if (updatingSearch) error.postValue(3)
        else{
            loadModeSearch = 1
            updatingSearch = true
            val news = repository.search(q, page)


            if (news == null) error.postValue(1)
            else if (news.isEmpty()) error.postValue(2)
            else{

                dataRepository.addHistory(q)
                _listHistory.add(0, q)
                listHistory.postValue(_listHistory)
                oldSizeSearch = _list.size

                lastSearch = q
                page = 0

                _list.clear()
                _list.addAll(news)

                listSearch.postValue(_list)
            }
        }

    }

    fun loadMoreNews() = viewModelScope.launch(Dispatchers.IO){
        loadModeSearch = 2
        updatingSearch = true
        val news = repository.search(lastSearch, page)

        if (news != null && news.isNotEmpty()){
            oldSizeSearch = _list.size
            page ++

            _list.addAll(news)

            listSearch.postValue(_list)
        }
        else error.postValue(
            if(news == null) 1
            else 2
        )
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

    fun deleteHistory(history: String, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        dataRepository.deleteHistory(history)
        _listHistory.remove(history)
        listHistory.postValue(_listHistory)
        deleteHistory.postValue(id)
    }
}