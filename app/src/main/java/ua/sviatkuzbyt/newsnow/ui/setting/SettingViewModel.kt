package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.SettingKey
import ua.sviatkuzbyt.newsnow.data.DataSetting

class SettingViewModel(application: Application): AndroidViewModel(application) {

    /**    VARIABLES AND INIT    */

    private val dataSetting = DataSetting(application)
    private var _settingList = arrayOf<SettingKey>()
    val settingList = MutableLiveData(_settingList)

    init {
        //set setting, fill list of setting
        viewModelScope.launch {
            _settingList = arrayOf(
                SettingKey(application.getString(R.string.language), dataSetting.getLanguage()),
                SettingKey(application.getString(R.string.region), dataSetting.getRegion())
            )
            settingList.postValue(_settingList)
        }
    }

    /**    PUBLIC FUNCTIONS    */

    //update view of language
    fun changeLanguage(){
        viewModelScope.launch{
            _settingList[0].value = dataSetting.getLanguage()
            settingList.postValue(_settingList)
        }
    }

    //update view of region
    fun changeRegion(){
        viewModelScope.launch{
            _settingList[1].value = dataSetting.getRegion()
            settingList.postValue(_settingList)
        }
    }
}