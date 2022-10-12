package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.repositories.ReviewRepository

class ReviewViewModel(application: Application): AndroidViewModel(application) {
    private val repository = ReviewRepository()
    private var _list = mutableListOf<NewsContainer>()
    val list = MutableLiveData<List<NewsContainer>>(_list)
    var newElements = 0
    var page = 0
    var isUpdated = false

    init {
        firstUpdate()
    }

    fun firstUpdate(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(0)
            _list.clear()
            _list.addAll(lastNews)
//            list.postValue().addAll(lastNews)
            list.postValue(_list)

            page = 1
            newElements = lastNews.size
        }
    }



    fun update(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(page)
            _list.addAll(lastNews)
            list.postValue(_list)
            page ++
            newElements = lastNews.size
        }
    }

}