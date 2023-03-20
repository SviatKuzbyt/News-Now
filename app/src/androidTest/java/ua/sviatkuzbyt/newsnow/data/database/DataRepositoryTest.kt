package ua.sviatkuzbyt.newsnow.data.database

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.NewsList


internal class DataRepositoryTest{
    private lateinit var dataRepository: DataRepository
    private val news = NewsList(
        "testNews",
        "translate",
        "12.00 20-03",
        true,
        null,
        "https://translate.google.com/?hl=uk"
    )
    private val history = "HiStOrY"

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        dataRepository = DataRepository(appContext)
    }

    @Test
    fun addSaveNews(){
        dataRepository.addSavedNews(news)
        val lastElementInDB = dataRepository.getSavedNews()[0]
        Assert.assertEquals(news.label, lastElementInDB.label)
    }

    @Test
    fun removeSavedNews(){
        dataRepository.removeSavedNews(news.link)
        val list = dataRepository.getSavedNews()

        var isSavedNews = false
        for (i in list){
            if(i.label == news.label){
                isSavedNews = true
                break
            }
        }

        Assert.assertNotEquals(isSavedNews, true)
    }


    @Test
    fun addHistory(){
        dataRepository.addHistory(history)
        Assert.assertEquals(history, dataRepository.getHistory()[0])
    }

    @Test
    fun deleteHistory(){
        dataRepository.deleteHistory(history)
        val list = dataRepository.getHistory()

        val isHistory = history in list
        Assert.assertEquals(isHistory, false)
    }
}