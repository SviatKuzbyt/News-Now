package ua.sviatkuzbyt.newsnow.data.loadlists

import ua.sviatkuzbyt.newsnow.data.database.DataSetting
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsTableRepository

class SearchRepository(
    savedNewsTableRepository: SavedNewsTableRepository,
    private val dataSetting: DataSetting
): NewsLoad(savedNewsTableRepository) {
    private val link = "https://newsdata.io/api/1/news?apikey=pub_11792063ac011beca171231a9b2ae554997ba"
    private var language = ""
    private var nextPage = ""
    private var lastSearchText = ""

    suspend fun setLanguage(){
        val lan = dataSetting.getLanguage()
        if (lan != "All") language = "&language=$lan"
    }

    fun loadNewList(searchText: String): MutableList<NewsList>{
        val news = getListFromUrl("$link$language&q=$searchText")
        nextPage = "&page=" + news.nextPage
        return news.newsList
    }

    fun loadMoreListList(list: MutableList<NewsList>): MutableList<NewsList>{
        if(nextPage == "null")
            throw Exception("No more result")
        else{
            val news = getListFromUrl( "$link$language&q=$lastSearchText$nextPage")
            nextPage = "&page=" + news.nextPage
            list.addAll(news.newsList)
            return list
        }
    }
}