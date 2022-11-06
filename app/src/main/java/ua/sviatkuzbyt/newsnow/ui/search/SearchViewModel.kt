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
    private val repository: SearchRepository
    private val dataRepository: DataRepository
    val error = MutableLiveData<Int>()
    //list search
    private val _list = mutableListOf<NewsContainer>()
    val listSearch = MutableLiveData<List<NewsContainer>>(_list)
    var oldSizeSearch = 0
    private var page = 0
    //list history
    private val _listHistory = mutableListOf<String>()
    val listHistory = MutableLiveData<List<String>>(_listHistory)
    var oldSizeHistory = 0
    var deleteHistory = 0
    //values for work
    private var lastSearch = ""
    var loadModeSearch = 0
    var updatingSearch = false
    var changeHistoryMode = 0

    init {
        //create repositories
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        repository = SearchRepository(data)

        //get history data
        viewModelScope.launch(Dispatchers.IO){
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
                if (q in _listHistory){
                    dataRepository.deleteHistory(q)
                    deleteHistory = _listHistory.indexOf(q)
                    _listHistory.removeAt(deleteHistory)
                    changeHistoryMode = 2
                    listHistory.postValue(_listHistory)
                }
                dataRepository.addHistory(q)
                _listHistory.add(0, q)
                changeHistoryMode = 1
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
            else 3
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
        changeHistoryMode = 2
        deleteHistory = id
        listHistory.postValue(_listHistory)
    }
}