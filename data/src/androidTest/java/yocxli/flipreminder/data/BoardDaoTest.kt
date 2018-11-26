package yocxli.flipreminder.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assert_
import org.junit.After

import org.junit.Before
import org.junit.Test

import yocxli.flipreminder.data.board.BoardDatabase
import yocxli.flipreminder.data.board.BoardEntity

class BoardDaoTest {

    private lateinit var database: BoardDatabase

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), BoardDatabase::class.java).build()
    }

    @After
    fun closeDb() = database.close()


    @Test
    fun listAll_hasNoRecord_returnsEmptyList() {
        val result = database.boardDao().listAll()

        assertThat(result).hasSize(0)
    }

    @Test
    fun listAll_hasOneRecord_returnsOneRecord() {
        database.boardDao().create(DEFAULT_BOARD)

        val result = database.boardDao().listAll()

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(DEFAULT_BOARD)
    }

    @Test
    fun listAll_hasTwoOrMoreRecords_returnsAllRecords() {
        database.boardDao().create(DEFAULT_BOARD)
        val secondBoard = BoardEntity(id = 2, name = "Second Board")
        database.boardDao().create(secondBoard)

        val result = database.boardDao().listAll()

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(DEFAULT_BOARD)
        assertThat(result[1]).isEqualTo(secondBoard)
    }

    @Test
    fun createAndFindById_succeedToCreateAndHasARecord_findByIdReturnsIt() {
        database.boardDao().create(DEFAULT_BOARD)
        val result = database.boardDao().findById(DEFAULT_BOARD_ID)

        assertThat(result).isEqualTo(DEFAULT_BOARD)
    }

    @Test
    fun deleteAndFindById_succeedToDeleteAndHasNoRecord_findByIdReturnsNull() {
        database.boardDao().create(DEFAULT_BOARD)

        database.boardDao().delete(DEFAULT_BOARD)
        val result = database.boardDao().findById(DEFAULT_BOARD_ID)
        assertThat(result).isNull()
    }

    @Test
    fun create_giveOnlyNameToEntity() {
        val givenEntity = BoardEntity(name = "Give Board Name")

        val rowId = database.boardDao().create(givenEntity)

        val result = database.boardDao().findById(1)
        assertThat(result).isEqualTo(givenEntity.copy(id = rowId))
    }

    @Test
    fun create_duplicateEntity_replaced() {
        val duplicateEntity = DEFAULT_BOARD.copy(createdDatetime = DEFAULT_BOARD.createdDatetime + 1)
        database.boardDao().create(DEFAULT_BOARD)

        database.boardDao().create(duplicateEntity)
        val result = database.boardDao().findById(1)

        assertThat(result).isEqualTo(duplicateEntity)
        assertThat(database.boardDao().count()).isEqualTo(1)
    }

    @Test
    fun count_noRecords_return0() {
        assertThat(database.boardDao().count()).isEqualTo(0)
    }

    @Test
    fun count_1Record_return1() {
        database.boardDao().create(DEFAULT_BOARD)

        val result = database.boardDao().count()

        assertThat(result).isEqualTo(1)
    }

    @Test
    fun update() {
        database.boardDao().create(DEFAULT_BOARD)
        val insertedBoardEntity = database.boardDao().findById(DEFAULT_BOARD_ID)
        val updateBoardEntity = insertedBoardEntity?.copy(name = "Update Board Name")
        if (updateBoardEntity == null) {
            assert_().fail("failed to prepare given data")
            return
        }

        database.boardDao().update(updateBoardEntity)

        val result = database.boardDao().findById(DEFAULT_BOARD_ID)

        assertThat(result).isEqualTo(updateBoardEntity)
    }

    companion object {
        private const val DEFAULT_BOARD_ID = 1L
        private const val DEFAULT_BOARD_NAME = "Default Board Name"
        private val DEFAULT_BOARD_CREATE_TIME = System.currentTimeMillis()
        private val DEFAULT_BOARD = BoardEntity(id = DEFAULT_BOARD_ID, name = DEFAULT_BOARD_NAME, createdDatetime = DEFAULT_BOARD_CREATE_TIME)
    }
}
