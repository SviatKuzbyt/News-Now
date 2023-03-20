package ua.sviatkuzbyt.newsnow.ui.review

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.NewsList
import ua.sviatkuzbyt.newsnow.data.database.DataRepository
import ua.sviatkuzbyt.newsnow.data.database.NewsDataBase
import ua.sviatkuzbyt.newsnow.data.DataSetting
import ua.sviatkuzbyt.newsnow.data.repositories.ReviewRepository

class ReviewViewModel(application: Application): AndroidViewModel(application) {

    /**    VARIABLES    */

    //repositories
    private lateinit var repository: ReviewRepository
    private val dataRepository: DataRepository
    private val setting = DataSetting(application)
    //list
    private var _list = mutableListOf<NewsList>()
    //operators values
    var newElements = 0
    var oldSize = 0
    var loadMode = 1
    var loaded = false
    //observe values
    val list = MutableLiveData<List<NewsList>>(_list)
    val error = MutableLiveData<Boolean>()

    /**    INIT    */
    init {
        //init data bases

        dataRepository = DataRepository(application)
        //repository = ReviewRepository(data)
        //start load
        firstUpdate()
    }

    /**    PUBLIC FUNCTIONS    */

    fun firstUpdate(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(true, setting.getRegionCode())

            if (lastNews != null){
                //change in local list
                oldSize = _list.size
                _list.clear()
                _list.addAll(lastNews)

                //change in global list
                loaded = true
                newElements = lastNews.size
                list.postValue(_list)

            } else error.postValue(true)
        }
    }

    fun update(){
        viewModelScope.launch(Dispatchers.IO){
            val lastNews = repository.getRecentlyNews(false, setting.getRegionCode())
            if (lastNews != null){
                _list.addAll(lastNews) //change in local list

                //change in global list
                loaded = true
                newElements = lastNews.size
                list.postValue(_list)
            }

            else error.postValue(true)
        }
    }

    fun addSavedNews(item: NewsList, updateSaved: Int){
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

    fun updateChanges() = viewModelScope.launch(Dispatchers.IO) {
        _list = repository.updateSaved(_list)
        list.postValue(_list)
    }
}