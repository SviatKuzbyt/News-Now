package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.other.DataSetting
import ua.sviatkuzbyt.newsnow.data.loadlists.ReviewRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.elements.NewsViewModel
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode
import ua.sviatkuzbyt.newsnow.ui.elements.SingleLiveEvent

class ReviewViewModel (private val application: Application): NewsViewModel(application){
    val newsList = MutableLiveData<MutableList<NewsList>>()
    val error = SingleLiveEvent<String>()
    var progressBarMode = MutableLiveData<ProgressBarMode>()
    var isAllDataNew = true

    private val repository = ReviewRepository(savedNewsRepository, DataSetting(application))

    init { loadNewNews() }

    fun loadNewNews() = viewModelScope.launch(Dispatchers.IO + handleException()){
        progressBarMode.postValue(ProgressBarMode.LoadNew)
        val list = repository.loadNewList()
        progressBarMode.postValue(ProgressBarMode.Nothing)
        newsList.postValue(list)
    }

    fun loadMoreNews() = viewModelScope.launch(Dispatchers.IO + handleException()){
        progressBarMode.postValue(ProgressBarMode.LoadMore)
        val list = repository.loadMoreListList(newsList.value!!)
        progressBarMode.postValue(ProgressBarMode.Nothing)
        isAllDataNew = false
        newsList.postValue(list)
    }

    private fun handleException(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            progressBarMode.postValue(ProgressBarMode.Nothing)
            error.postValue(application.getString(R.string.internet_error))
        }
    }
}