package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.changeSetting
import ua.sviatkuzbyt.newsnow.data.SettingValues
import ua.sviatkuzbyt.newsnow.data.DataSetting
import ua.sviatkuzbyt.newsnow.data.repositories.SettingRepository

class SettingValuesViewModel(application: Application): AndroidViewModel(application) {

    /**    VARIABLES    */
    //repositories
    private val repository: SettingRepository = SettingRepository(application)
    private val dataSetting = DataSetting(application)

    val context = application
    private var _valuesList = emptyList<SettingValues>()
    //observes values
    val valuesList = MutableLiveData(_valuesList)
    val finish = MutableLiveData<Boolean>()

    /**    PUBLIC FUNCTIONS    */

    //get list of language from setting db
    fun loadLanguagesList(){
        _valuesList = repository.listLanguage
        valuesList.value = _valuesList
    }

    //get list of region from setting db
    fun loadRegionsList(){
        _valuesList = repository.listRegion
        valuesList.value = _valuesList
    }

    //set value in setting db
    fun updateKey(value: String, key: String, code: String, keyShort: String){
        viewModelScope.launch {
            dataSetting.setValue(value, key, code, keyShort)
            changeSetting = key
            finish.postValue(true)
        }
    }


}