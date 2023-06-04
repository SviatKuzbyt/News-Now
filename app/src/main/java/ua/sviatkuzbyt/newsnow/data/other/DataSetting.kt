package ua.sviatkuzbyt.newsnow.data.other

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ua.sviatkuzbyt.newsnow.dataStore
import java.util.*

class DataSetting(val context: Context) {
    private val regionCode = stringPreferencesKey("region_code")
    private val searchInAllRegion = booleanPreferencesKey("search_all")

    suspend fun getRegionCode():String{
        return context.dataStore.data.map {
            it[regionCode] ?: Locale.getDefault().country
        }.first()
    }

    suspend fun getSearchAll(): Boolean{
        return context.dataStore.data.map {
            it[searchInAllRegion] ?: true
        }.first()
    }

    suspend fun setRegion(code: String) {
        context.dataStore.edit { settings ->
            settings[regionCode] = code
        }
    }

    suspend fun setSearchInAll(param: Boolean) {
        context.dataStore.edit { settings ->
            settings[searchInAllRegion] = param
        }
    }
}