//package ua.sviatkuzbyt.newsnow.ui.saved
//
//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import ua.sviatkuzbyt.newsnow.changeSavedNewsForReview
//import ua.sviatkuzbyt.newsnow.changeSavedNewsForSearch
//import ua.sviatkuzbyt.newsnow.data.repositories.SavedNewsDataBase
//import ua.sviatkuzbyt.newsnow.data.database.room.SavedNewsEntity
//
//class SavedViewModel(application: Application): AndroidViewModel(application) {
//
//    /**    VARIABLES    */
//    private lateinit var  savedNewsDataBase: SavedNewsDataBase
//    private val _list = mutableListOf<SavedNewsEntity>()
//    val list = MutableLiveData<List<SavedNewsEntity>>(_list)
//    val deleteElement = MutableLiveData<Int>()
//
//    /**    INIT    */
//    init {
//        //initializing data base
////        val data = NewsDataBase.getDatabase(application).request()
////        dataRepository = DataRepository(data, application)
//        //load news
//        getSavedNews()
//    }
//
//    /**    PUBLIC FUNCTIONS    */
//
//    private fun getSavedNews(){
//        viewModelScope.launch(Dispatchers.IO) {
//            _list.addAll(savedNewsDataBase.getSavedNews())
//            list.postValue(_list)
//        }
//    }
//
//    fun updateSavedNews(){
//        _list.clear()
//        getSavedNews()
//    }
//
//    fun deleteSavedNews(link: String, id: Int){
//        viewModelScope.launch(Dispatchers.IO){
//            _list.removeAt(id)
//            deleteElement.postValue(id)
//            savedNewsDataBase.removeSavedNews(link)
//
//            changeSavedNewsForReview = true
//            changeSavedNewsForSearch = true
//        }
//    }
//}