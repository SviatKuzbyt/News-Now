package ua.sviatkuzbyt.newsnow.data

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.loadlists.NewsLoad
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.other.NewsList

internal class NewsLoadTest{
    lateinit var newsLoad: NewsLoad

    @Before
    fun init(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataBaseRepository = DataBaseRepository(appContext)
        newsLoad = NewsLoad(dataBaseRepository)
    }

    @Test
    fun checkLoadAndConvertData(): Unit = runBlocking{
        launch {
            val link = "https://newsdata.io/api/1/news?apikey=pub_11792063ac011beca171231a9b2ae554997ba"
            val list = newsLoad.getListFromUrl(link)
            println("RESULT: $list")
            Assert.assertNotEquals(list, emptyList<NewsList>())
        }
    }
}