package ua.sviatkuzbyt.newsnow.data.loadlists

import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.database.DataSetting

class SearchRepository(
    dataBaseRepository: DataBaseRepository,
    private val dataSetting: DataSetting
): NewsLoad(dataBaseRepository) {
    private val link = "https://newsdata.io/api/1/news?apikey=pub_11792063ac011beca171231a9b2ae554997ba"

    private var nextPage = ""
    private var lastSearchText = ""
    private var country = ""

    suspend fun setCountry(isAll: Boolean){
        if(!isAll)
            country = "&country=" + dataSetting.getRegionCode()
    }

    suspend fun loadNewList(searchText: String): MutableList<NewsList>{
        val news = getListFromUrl("$link&q=$searchText$country")
        nextPage = "&page=" + news.nextPage
        lastSearchText = searchText
        return news.newsList
    }

    suspend fun loadMoreListList(list: MutableList<NewsList>): MutableList<NewsList>{
        if(nextPage == "&page=null")
            throw Exception("No more result")
        else{
            val news = getListFromUrl( "$link&q=$lastSearchText$country$nextPage")
            nextPage = "&page=" + news.nextPage
            list.addAll(news.newsList)
            return list
        }
    }
}