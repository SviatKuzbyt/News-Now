package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.NewsContainer
import ua.sviatkuzbyt.newsnow.data.review.ReviewRepository

class ReviewViewModel(application: Application): AndroidViewModel(application) {
    private val repository = ReviewRepository()
    val list = MutableLiveData<List<NewsContainer>>()

    init {
        update()
    }

    fun update(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews()
            list.postValue(lastNews)
        }
    }
}