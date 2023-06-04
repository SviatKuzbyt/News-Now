package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.other.DataSetting

class SettingViewModel(application: Application): AndroidViewModel(application) {
    private val dataSetting = DataSetting(application)
    val searchInAll = MediatorLiveData<Boolean>()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            searchInAll.postValue(dataSetting.getSearchAll())
        }
    }

    fun setSearchInAll(param: Boolean){
        searchInAll.value = param
        viewModelScope.launch(Dispatchers.IO) {
            dataSetting.setSearchInAll(param)
        }
    }
}