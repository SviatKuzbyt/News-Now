package ua.sviatkuzbyt.newsnow.data.other

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.sviatkuzbyt.newsnow.data.database.SearchHistoryDBRepository


internal class SearchHistoryDBRepositoryTest{
    private lateinit var searchHistoryDBRepository: SearchHistoryDBRepository
    private val history = "HiStOrY"

    @Before
    fun setup() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        searchHistoryDBRepository = SearchHistoryDBRepository(appContext)
    }

    @Test
    fun addHistory(){
        searchHistoryDBRepository.addHistory(history)
        Assert.assertEquals(history, searchHistoryDBRepository.getHistory()[0])
    }

    @Test
    fun deleteHistory(){
        searchHistoryDBRepository.deleteHistory(history)
        val list = searchHistoryDBRepository.getHistory()

        val isHistory = history in list
        Assert.assertEquals(isHistory, false)
    }
}