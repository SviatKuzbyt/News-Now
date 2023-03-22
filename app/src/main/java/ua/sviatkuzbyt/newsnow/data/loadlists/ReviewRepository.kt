package ua.sviatkuzbyt.newsnow.data.loadlists

import ua.sviatkuzbyt.newsnow.data.database.DataSetting
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsDBRepository

class ReviewRepository(
    savedNewsDBRepository: SavedNewsDBRepository,
    private val dataSetting: DataSetting
):
    NewsLoad(savedNewsDBRepository){

    private val link = "https://newsdata.io/api/1/news?apikey=pub_11792063ac011beca171231a9b2ae554997ba"
    private var region = "&country=US"
    private var nextPage = ""

    suspend fun setRegion(){
        region = "&country=" + dataSetting.getRegionCode()
    }

    fun loadNewList(): MutableList<NewsList>{
        val news = getListFromUrl(link + region)
        nextPage = "&page=" + news.nextPage
        return news.newsList
    }

    fun loadMoreListList(list: MutableList<NewsList>): MutableList<NewsList>{
        val news = getListFromUrl(link + region + nextPage)
        nextPage = "&page=" + news.nextPage
        list.addAll(news.newsList)
        return list
    }
}