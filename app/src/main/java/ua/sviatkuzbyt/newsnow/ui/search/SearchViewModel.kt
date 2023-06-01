package ua.sviatkuzbyt.newsnow.ui.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.loadlists.SearchRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.ui.elements.ProgressBarMode
import ua.sviatkuzbyt.newsnow.ui.elements.NewsViewModel
import ua.sviatkuzbyt.newsnow.ui.elements.SingleLiveEvent

class SearchViewModel(private val application: Application): NewsViewModel(application){
    val newsList = MutableLiveData<MutableList<NewsList>>()
    val error = SingleLiveEvent<String>()
    var progressBarMode = MutableLiveData<ProgressBarMode>()
    var isAllDataNew = true

    val dataBaseRepository = DataBaseRepository(application) //temp
    private val repository = SearchRepository(dataBaseRepository)
    private var loadNews: Job? = null

    fun loadNewNews(request: String) {
        cancelAndLoadNews {
            progressBarMode.postValue(ProgressBarMode.LoadNew)
            val list = repository.loadNewList(request)
            newsList.postValue(list)
        }
    }

    fun loadMoreNews() {
        cancelAndLoadNews {
            progressBarMode.postValue(ProgressBarMode.LoadMore)
            val list = repository.loadMoreListList(newsList.value!!)
            progressBarMode.postValue(ProgressBarMode.Nothing)
            isAllDataNew = false
            newsList.postValue(list)
        }
    }

    private fun cancelAndLoadNews(operation: suspend () -> Unit) {
        loadNews?.cancel()
        loadNews = viewModelScope.launch(Dispatchers.IO + handleException()) {
            operation()
            progressBarMode.postValue(ProgressBarMode.Nothing)
        }
    }

    private fun handleException(): CoroutineExceptionHandler {
        return CoroutineExceptionHandler { _, throwable ->
            progressBarMode.postValue(ProgressBarMode.Nothing)
            val message =
                if(throwable.message == "No more result") application.getString(R.string.no_more_result)
                else application.getString(R.string.internet_error)
            error.postValue(message)
        }
    }
}