package ua.sviatkuzbyt.newsnow.data.loadlists

import ua.sviatkuzbyt.newsnow.data.other.DataSetting
import ua.sviatkuzbyt.newsnow.data.other.NewsList
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.ui.SharedData

class ReviewRepository(
    dataBaseRepository: DataBaseRepository,
    private val dataSetting: DataSetting
):
    NewsLoad(dataBaseRepository){

    private val link = "https://newsdata.io/api/1/news?apikey=pub_11792063ac011beca171231a9b2ae554997ba"
    private var region = ""
    private var nextPage = ""

    private suspend fun setRegion(){
        if(region.isEmpty() || SharedData.isChangeRegion){
            region = "&country=" + dataSetting.getRegionCode()
            SharedData.isChangeRegion = false
        }
    }

    suspend fun loadNewList(): MutableList<NewsList>{
        setRegion()
        val news = getListFromUrl(link + region)
        nextPage = "&page=" + news.nextPage
        return news.newsList
    }

    suspend fun loadMoreListList(list: MutableList<NewsList>): MutableList<NewsList>{
        val news = getListFromUrl(link + region + nextPage)
        nextPage = "&page=" + news.nextPage
        list.addAll(news.newsList)
        return list
    }
}