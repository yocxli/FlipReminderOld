package yocxli.flipreminder.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.CoreMatchers.`is`
import org.junit.After

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import yocxli.flipreminder.data.board.BoardDatabase
import yocxli.flipreminder.data.board.BoardEntity

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class BoardDaoTest {

    private lateinit var database: BoardDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), BoardDatabase::class.java).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertBoardAndFindById() {
        database.boardDao().create(DEFAULT_BOARD)

        val reuslt = database.boardDao().findById(DEFAULT_BOARD_ID)

        assertThat(reuslt, `is`(DEFAULT_BOARD))
    }

    companion object {
        private const val DEFAULT_BOARD_ID = 1
        private const val DEFAULT_BOARD_NAME = "Default Board Name"
        private val DEFAULT_BOARD_CREATE_TIME = System.currentTimeMillis()
        private val DEFAULT_BOARD = BoardEntity(id = DEFAULT_BOARD_ID, name = DEFAULT_BOARD_NAME, createdDatetime = DEFAULT_BOARD_CREATE_TIME)
    }
}
