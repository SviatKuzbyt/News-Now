package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.database.DataRepository
import ua.sviatkuzbyt.newsnow.data.database.RequestsNewsData
import ua.sviatkuzbyt.newsnow.data.database.SaveNewsDataBase
import ua.sviatkuzbyt.newsnow.data.repositories.ReviewRepository

class ReviewViewModel(application: Application): AndroidViewModel(application) {
    private val repository: ReviewRepository
    private val dataRepository: DataRepository
    private var _list = mutableListOf<NewsContainer>()
    val list = MutableLiveData<List<NewsContainer>>(_list)
    var newElements = 0
    private var page = 0
    var oldSize = 0
    var isUpdated = false
    val error = MutableLiveData<Boolean>()

    init {
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        repository = ReviewRepository(data)
        firstUpdate()
    }

    fun firstUpdate(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(0)
            if (lastNews != null){
                oldSize = _list.size
                _list.clear()
                _list.addAll(lastNews)
                list.postValue(_list)
                page = 1
                newElements = lastNews.size
            } else error.postValue(true)
        }
    }

    fun update(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(page)
            if (lastNews != null){
                _list.addAll(lastNews)
                list.postValue(_list)
                page ++
                newElements = lastNews.size
            }
            else error.postValue(true)
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