package ua.sviatkuzbyt.newsnow.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.data.other.DataSetting
import ua.sviatkuzbyt.newsnow.data.other.SettingValues
import ua.sviatkuzbyt.newsnow.ui.SharedData
import java.util.*

class ChooseSettingViewModel(application: Application): AndroidViewModel(application){
    private val dataSetting = DataSetting(application)
    var currentRegion = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO){
            currentRegion.postValue(dataSetting.getRegionCode())
        }
    }

    fun setRegionAndFinish(code: String){
        SharedData.isChangeRegion = true
        viewModelScope.launch(Dispatchers.IO){
            dataSetting.setRegion(code)
            currentRegion.postValue("finish")
        }
    }

    val listRegion by lazy {
        listOf(
            SettingValues(application.getString(R.string.system), Locale.getDefault().country),
            SettingValues("Argentina", "ar"),
            SettingValues("Australia", "au"),
            SettingValues("Austria", "at"),
            SettingValues("Azerbaijan", "az"),
            SettingValues("Bangladesh", "bd"),
            SettingValues("Belarus", "by"),
            SettingValues("Belgium", "be"),
            SettingValues("Bolivia", "bo"),
            SettingValues("Brazil", "br"),
            SettingValues("Bulgaria", "bg"),
            SettingValues("Canada", "ca"),
            SettingValues("Chile", "cl"),
            SettingValues("China", "cn"),
            SettingValues("Colombia", "co"),
            SettingValues("Costa Rica", "cr"),
            SettingValues("Cuba", "cu"),
            SettingValues("Czech republic", "cz"),
            SettingValues("Denmark", "dk"),
            SettingValues("Dominican Republic", "do"),
            SettingValues("Ecuador", "ec"),
            SettingValues("Egypt", "eg"),
            SettingValues("Estonia", "ee"),
            SettingValues("Ethiopia", "et"),
            SettingValues("Finland", "fi"),
            SettingValues("France", "fr"),
            SettingValues("Germany", "de"),
            SettingValues("Greece", "gr"),
            SettingValues("Hong kong", "hk"),
            SettingValues("Hungary", "hu"),
            SettingValues("India", "in"),
            SettingValues("Indonesia", "id"),
            SettingValues("Iraq", "iq"),
            SettingValues("Ireland", "ie"),
            SettingValues("Israel", "il"),
            SettingValues("Italy", "it"),
            SettingValues("Japan", "jp"),
            SettingValues("Jordan", "jo"),
            SettingValues("Kazakhstan", "kz"),
            SettingValues("Kuwait", "kw"),
            SettingValues("Latvia", "lv"),
            SettingValues("Lebanon", "lb"),
            SettingValues("Lithuania", "lt"),
            SettingValues("Malaysia", "my"),
            SettingValues("Mexico", "mx"),
            SettingValues("Morocco", "ma"),
            SettingValues("Myanmar", "mm"),
            SettingValues("Netherland", "nl"),
            SettingValues("New zealand", "nz"),
            SettingValues("Nigeria", "ng"),
            SettingValues("North korea", "kp"),
            SettingValues("Norway", "no"),
            SettingValues("Pakistan", "pk"),
            SettingValues("Peru", "pe"),
            SettingValues("Philippines", "ph"),
            SettingValues("Poland", "pl"),
            SettingValues("Portugal", "pt"),
            SettingValues("Puerto Rico", "pr"),
            SettingValues("Romania", "ro"),
            SettingValues("Russia", "ru"),
            SettingValues("Saudi arabia", "sa"),
            SettingValues("Serbia", "rs"),
            SettingValues("Singapore", "sg"),
            SettingValues("Slovakia", "sk"),
            SettingValues("Slovenia", "si"),
            SettingValues("South africa", "za"),
            SettingValues("South korea", "kr"),
            SettingValues("Spain", "es"),
            SettingValues("Sudan", "sd"),
            SettingValues("Sweden", "se"),
            SettingValues("Switzerland", "ch"),
            SettingValues("Taiwan", "tw"),
            SettingValues("Tanzania", "tz"),
            SettingValues("Thailand", "th"),
            SettingValues("Turkey", "tr"),
            SettingValues("Ukraine", "ua"),
            SettingValues("United arab emirates", "ae"),
            SettingValues("United kingdom", "gb"),
            SettingValues("United states of america", "us"),
            SettingValues("Venezuela", "ve"),
            SettingValues("Vietnam", "vi")
        )
    }
}