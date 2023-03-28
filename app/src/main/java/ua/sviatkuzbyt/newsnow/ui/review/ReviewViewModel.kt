package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.database.DataSetting
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsTableRepository
import ua.sviatkuzbyt.newsnow.data.loadlists.ReviewRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.elements.SingleLiveEvent

sealed class ProgressBarMode {
    object AnythingView: ProgressBarMode()
    object RefreshView: ProgressBarMode()
    object LoadMoreView: ProgressBarMode()
}

class ReviewViewModel (private val application: Application): AndroidViewModel(application){
    val newsList = MutableLiveData<MutableList<NewsList>>()
    val error = SingleLiveEvent<String>()
    var progressBarMode = MutableLiveData<ProgressBarMode>()
    var isAllDataNew = true

    private val savedNewsTableRepository = SavedNewsTableRepository(application) //temp
    private val dataSetting = DataSetting(application) //temp
    private val repository = ReviewRepository(savedNewsTableRepository, dataSetting)

    init {
        loadNewNews()
    }

    fun loadNewNews() = viewModelScope.launch(Dispatchers.IO + handleException()){
        progressBarMode.postValue(ProgressBarMode.RefreshView)
        val list = repository.loadNewList()
        progressBarMode.postValue(ProgressBarMode.AnythingView)
        isAllDataNew = true
        newsList.postValue(list)
    }

    fun loadMoreNews() = viewModelScope.launch(Dispatchers.IO + handleException()){
        progressBarMode.postValue(ProgressBarMode.LoadMoreView)
        val list = repository.loadMoreListList(newsList.value!!)
        progressBarMode.postValue(ProgressBarMode.AnythingView)
        isAllDataNew = false
        newsList.postValue(list)
    }

    private fun handleException(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, _ ->
            progressBarMode.postValue(ProgressBarMode.AnythingView)
            error.postValue(application.getString(R.string.internet_error))
        }
    }
}