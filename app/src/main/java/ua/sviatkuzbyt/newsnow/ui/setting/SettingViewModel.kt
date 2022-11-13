package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.SettingKey
import ua.sviatkuzbyt.newsnow.data.repositories.DataSetting
import ua.sviatkuzbyt.newsnow.dataStore


class SettingViewModel(application: Application): AndroidViewModel(application) {
    private val dataSetting = DataSetting(application)
    private var _settingList = arrayOf<SettingKey>()
    val settingList = MutableLiveData(_settingList)

    init {
        viewModelScope.launch {
            _settingList = arrayOf(
                SettingKey(application.getString(R.string.language), dataSetting.getLanguage()),
                SettingKey(application.getString(R.string.region), dataSetting.getRegion())
            )
            settingList.postValue(_settingList)
        }
    }

    fun changeLanguage(){
        viewModelScope.launch{
            _settingList[0].value = dataSetting.getLanguage()
            settingList.postValue(_settingList)
        }
    }

    fun changeRegion(){
        viewModelScope.launch{
            _settingList[1].value = dataSetting.getRegion()
            settingList.postValue(_settingList)
        }
    }
}