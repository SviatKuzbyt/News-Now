package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.data.SettingValues
import ua.sviatkuzbyt.newsnow.data.repositories.DataSetting
import ua.sviatkuzbyt.newsnow.data.repositories.SettingRepository
import kotlin.coroutines.coroutineContext

class SettingValuesViewModel(application: Application): AndroidViewModel(application) {
    private var _valuesList = emptyList<SettingValues>()
    private val repository: SettingRepository = SettingRepository(application)
    val valuesList = MutableLiveData(_valuesList)
    val context = application
    val finish = MutableLiveData<Boolean>()
    private val dataSetting = DataSetting(application)

    fun loadLanguagesList(){
        _valuesList = repository.listLanguage
        valuesList.value = _valuesList
    }

    fun loadRegionsList(){
        _valuesList = repository.listRegion
        valuesList.value = _valuesList
    }

    fun updateKey(value: String, key: String, code: String, keyShort: String){
        viewModelScope.launch {
            dataSetting.setValue(value, key, code, keyShort)
            change = key
            finish.postValue(true)
        }
    }
}