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
import ua.sviatkuzbyt.newsnow.data.DataSetting
import ua.sviatkuzbyt.newsnow.data.repositories.SearchRepository

class SearchViewModel(application: Application): AndroidViewModel(application) {

    /*    VARIABLES    */

    //repositories
    private val repository: SearchRepository
    private val dataRepository: DataRepository
    val setting = DataSetting(application)
    //private vars
    private val _list = mutableListOf<NewsContainer>()
    private val _listHistory = mutableListOf<String>()
    private var page = 0
    private var lastSearch = ""
    //observe values
    val listSearch = MutableLiveData<List<NewsContainer>>(_list)
    val listHistory = MutableLiveData<List<String>>(_listHistory)
    val error = MutableLiveData<Int>()
    //control changes
    var oldSizeSearch = 0
    var oldSizeHistory = 0
    var deleteHistory = 0
    //mods
    var updatingSearch = false
    var loadMore = false
    var loadModeSearch = 0
    var changeHistoryMode = 0

    /*    INIT    */

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

    /*    PUBLIC FUNCTIONS    */

    fun getNews(q: String = lastSearch) = viewModelScope.launch(Dispatchers.IO) {
        if (updatingSearch) error.postValue(3)
        else{
            //start load
            updatingSearch = true
            val news = repository.search(q, setting.getLanguageCode(), 0)
            //exceptions handling
            if (news == null) error.postValue(1)
            else if (news.isEmpty()) error.postValue(2)

            //put news
            else{
                //delete the last same search in history
                if (q in _listHistory) deleteHistory(q, _listHistory.indexOf(q))

                //add new search in history
                dataRepository.addHistory(q)
                _listHistory.add(0, q)
                changeHistoryMode = 1
                listHistory.postValue(_listHistory)

                //change operators values
                oldSizeSearch = _list.size
                lastSearch = q
                page = 1
                loadModeSearch = 1

                //publish news
                _list.clear()
                _list.addAll(news)
                listSearch.postValue(_list)
            }
        }

    }

    fun loadMoreNews() = viewModelScope.launch(Dispatchers.IO){
        //load news
        updatingSearch = true
        val news = repository.search(lastSearch, setting.getLanguageCode(), page)

        if (news != null && news.isNotEmpty()){
            //change operators values
            oldSizeSearch = _list.size
            page ++
            loadModeSearch = 2

            //publish news
            _list.addAll(news)
            listSearch.postValue(_list)
        }
        //exception handling
        else error.postValue(3)
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
        _listHistory.removeAt(id)
        changeHistoryMode = 2
        deleteHistory = id
        listHistory.postValue(_listHistory)
    }
}