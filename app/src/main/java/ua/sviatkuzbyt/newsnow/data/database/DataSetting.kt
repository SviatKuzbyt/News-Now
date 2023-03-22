package ua.sviatkuzbyt.newsnow.data.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ua.sviatkuzbyt.newsnow.R
import ua.sviatkuzbyt.newsnow.dataStore
import java.util.*

class DataSetting(val context: Context) {
    private val region = stringPreferencesKey("region")
    private val language = stringPreferencesKey("language")
    private val regionCode = stringPreferencesKey("region_code")
    private val languageCode = stringPreferencesKey("language_code")

    suspend fun getRegion():String{
        return  context.dataStore.data.map {
            it[region] ?: context.getString(R.string.system)
        }.first()
    }

    suspend fun getRegionCode():String{
        return context.dataStore.data.map {
            it[regionCode] ?: Locale.getDefault().country
        }.first()
    }

    suspend fun getLanguage():String{
        return  context.dataStore.data.map {
            it[language] ?: context.getString(R.string.all)
        }.first()
    }

    suspend fun getLanguageCode():String{
        return  context.dataStore.data.map {
            it[languageCode] ?: ""
        }.first()
    }

    suspend fun setValue(value: String, key: String, code: String,  keyShort: String) {
        context.dataStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
            settings[stringPreferencesKey(keyShort)] = code
        }
    }
}