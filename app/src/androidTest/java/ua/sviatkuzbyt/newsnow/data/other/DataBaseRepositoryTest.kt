package ua.sviatkuzbyt.newsnow.data.other

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository


internal class DataBaseRepositoryTest{
    private lateinit var dataBaseRepository: DataBaseRepository
    private val news = NewsList(
        "testNews",
        "translate",
        "12.00 20-03",
        true,
        null,
        "https://translate.google.com/?hl=uk"
    )


    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        dataBaseRepository = DataBaseRepository(appContext)
    }

    @Test
    fun addSaveNews(){
        dataBaseRepository.addSavedNews(news)
        val lastElementInDB = dataBaseRepository.getSavedNews()[0]
        Assert.assertEquals(news.label, lastElementInDB.label)
    }

    @Test
    fun removeSavedNews(){
        dataBaseRepository.removeSavedNews(news.link)
        val list = dataBaseRepository.getSavedNews()

        var isSavedNews = false
        for (i in list){
            if(i.label == news.label){
                isSavedNews = true
                break
            }
        }

        Assert.assertNotEquals(isSavedNews, true)
    }
}