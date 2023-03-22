package ua.sviatkuzbyt.newsnow.data.other

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.database.SavedNewsDBRepository


internal class SavedNewsDBRepositoryTest{
    private lateinit var savedNewsDBRepository: SavedNewsDBRepository
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
        savedNewsDBRepository = SavedNewsDBRepository(appContext)
    }

    @Test
    fun addSaveNews(){
        savedNewsDBRepository.addSavedNews(news)
        val lastElementInDB = savedNewsDBRepository.getSavedNews()[0]
        Assert.assertEquals(news.label, lastElementInDB.label)
    }

    @Test
    fun removeSavedNews(){
        savedNewsDBRepository.removeSavedNews(news.link)
        val list = savedNewsDBRepository.getSavedNews()

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