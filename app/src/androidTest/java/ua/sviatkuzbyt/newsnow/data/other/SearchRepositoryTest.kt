package ua.sviatkuzbyt.newsnow.data.other

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.loadlists.SearchRepository


internal class SearchRepositoryTest{
    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        val dataBaseRepository = DataBaseRepository(appContext)
//        val dataSetting = DataSetting(appContext)
//        repository = SearchRepository(dataBaseRepository, dataSetting)
//
//        runBlocking {
//            launch {
//                repository.setLanguage()
//            }
//        }
    }

    @Test
    fun checkLoadNewList(){
        runBlocking {
            launch {
                val list = repository.loadNewList("usa")
                printList(list)
                Assert.assertNotEquals(null, list)
            }
        }
    }

    @Test
    fun checkLoadMoreList(){
        runBlocking {
            launch {
                var list = repository.loadNewList("usa")
                for (i in 0..3){
                    list = repository.loadMoreListList(list)
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