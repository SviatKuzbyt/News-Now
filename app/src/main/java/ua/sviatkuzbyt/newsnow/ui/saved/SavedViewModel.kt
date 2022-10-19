package ua.sviatkuzbyt.newsnow.ui.saved

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.database.DataRepository
import ua.sviatkuzbyt.newsnow.data.database.SaveNewsDataBase
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsEntity

class SavedViewModel(application: Application): AndroidViewModel(application) {
    private val _list = mutableListOf<SavedNewsEntity>()
    val list = MutableLiveData<List<SavedNewsEntity>>(_list)
    val deleteElement = MutableLiveData<Int>()
    private val dataRepository: DataRepository

    init {
        val data = SaveNewsDataBase.getDatabase(application).request()
        dataRepository = DataRepository(data)
        getSavedNews()
    }

    private fun getSavedNews(){
        viewModelScope.launch(Dispatchers.IO) {
            _list.addAll(dataRepository.getData())
            list.postValue(_list)
            Log.v("Observe", "getSavedNews")
        }
    }

    fun updateSavedNews(){
        _list.clear()
        getSavedNews()
    }

    fun deleteSavedNews(link: String, id: Int){
        viewModelScope.launch(Dispatchers.IO){
            _list.removeAt(id)
            deleteElement.postValue(id)
            dataRepository.removeSavedNews(link)
            list.postValue(_list)
            Log.v("Observe", "deleteSavedNews")
        }
    }
}