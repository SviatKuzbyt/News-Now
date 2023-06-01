package ua.sviatkuzbyt.newsnow.data.other

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.database.DataSetting
import ua.sviatkuzbyt.newsnow.data.database.DataBaseRepository
import ua.sviatkuzbyt.newsnow.data.loadlists.ReviewRepository

internal class ReviewRepositoryTest{
    private lateinit var reviewRepository: ReviewRepository

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val dataBaseRepository = DataBaseRepository(appContext)
        val dataSetting = DataSetting(appContext)
        reviewRepository = ReviewRepository(dataBaseRepository, dataSetting)
    }

    @Test
    fun checkLoadNewList(){
        runBlocking {
            launch {
                val list = reviewRepository.loadNewList()
                printList(list)
                Assert.assertNotEquals(null, list)
            }
        }
    }

    @Test
    fun checkLoadMoreList(){
        runBlocking {
            launch {
                var list = reviewRepository.loadNewList()
                for (i in 0..3){
                    list = reviewRepository.loadMoreListList(list)
                }
                printList(list)
                Assert.assertNotEquals(null, list)
            }
        }
    }

    private fun printList(list: MutableList<NewsList>){
        var string = "RESULT:"
        list.forEach {
            string += "\n$it\n"
        }
        println(string)
    }
}