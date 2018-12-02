package yocxli.flipreminder.data

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assert_
import org.junit.After

import org.junit.Before
import org.junit.Test

import yocxli.flipreminder.data.board.BoardDatabase
import yocxli.flipreminder.data.board.BoardEntity
import yocxli.flipreminder.data.board.CardDao
import yocxli.flipreminder.data.board.CardEntity

class CardDaoTest {

    private lateinit var database: BoardDatabase
    private lateinit var cardDao: CardDao

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), BoardDatabase::class.java).build()
        cardDao = database.cardDao()
    }

    @After
    fun closeDb() = database.close()


    @Test
    fun getCardsOf_hasNoRecord_returnsEmptyList() {
        val result = cardDao.getCardsOf(DEFAULT_BOARD_ID)

        assertThat(result).hasSize(0)
    }

    @Test
    fun getCardsOf_hasOneRecord_returnsOneRecord() {
        val boardId = database.boardDao().create(DEFAULT_BOARD)
        cardDao.create(NON_FLIPPED_CARD_1, boardId)

        val result = cardDao.getCardsOf(boardId)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(NON_FLIPPED_CARD_1)
    }

    @Test
    fun getCardsOf_hasTwoOrMoreRecords_returnsAllRecords() {
        populateTestData()

        val result = cardDao.getCardsOf(DEFAULT_BOARD_ID)

        assertThat(result).hasSize(4)
        assertThat(result[0]).isEqualTo(NON_FLIPPED_CARD_1)
        assertThat(result[1]).isEqualTo(NON_FLIPPED_CARD_2)
        assertThat(result[2]).isEqualTo(FLIPPED_CARD_1)
        assertThat(result[3]).isEqualTo(FLIPPED_CARD_2)
    }

    @Test
    fun getFlippedCardsOf_hasTwoOrMoreRecords_returnsAllRecords() {
        populateTestData()

        val result = cardDao.getFlippedCardsOf(DEFAULT_BOARD_ID)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(FLIPPED_CARD_1)
        assertThat(result[1]).isEqualTo(FLIPPED_CARD_2)
    }

    @Test
    fun insertAndFindById_succeedToCreateAndHasARecord_findByIdReturnsIt() {
        cardDao.insert(NON_FLIPPED_CARD_1)
        val result = cardDao.findById(DEFAULT_CARD_ID)

        assertThat(result).isEqualTo(NON_FLIPPED_CARD_1)
    }

    @Test
    fun deleteAndFindById_succeedToDeleteAndHasNoRecord_findByIdReturnsNull() {
        cardDao.insert(NON_FLIPPED_CARD_1)

        cardDao.delete(NON_FLIPPED_CARD_1)
        val result = cardDao.findById(DEFAULT_CARD_ID)

        assertThat(result).isNull()
    }

    @Test
    fun insert_giveOnlyLabelToEntity() {
        val givenEntity = CardEntity(label = "Give Board Name")

        val rowId = cardDao.insert(givenEntity)

        val result = cardDao.findById(rowId)
        assertThat(result).isEqualTo(givenEntity.copy(id = rowId))
    }

    @Test
    fun insert_duplicateEntity_aborted() {
        val duplicateEntity = NON_FLIPPED_CARD_1.copy(createdDatetime = NON_FLIPPED_CARD_1.createdDatetime + 1)
        cardDao.insert(NON_FLIPPED_CARD_1)

        try {
            cardDao.insert(duplicateEntity)
        } catch (e: SQLiteConstraintException) {
            // do nothing
        }

        val result = cardDao.findById(DEFAULT_CARD_ID)
        assertThat(result).isEqualTo(NON_FLIPPED_CARD_1)
    }

    @Test
    fun update() {
        cardDao.insert(NON_FLIPPED_CARD_1)
        val insertedBoardEntity = cardDao.findById(DEFAULT_CARD_ID)
        val updateBoardEntity = insertedBoardEntity?.copy(label = "Update Board Name")
        if (updateBoardEntity == null) {
            assert_().fail("failed to prepare given data")
            return
        }

        cardDao.update(updateBoardEntity)

        val result = cardDao.findById(DEFAULT_CARD_ID)

        assertThat(result).isEqualTo(updateBoardEntity)
    }

    companion object {
        private const val DEFAULT_CARD_ID = 1L
        private val NON_FLIPPED_CARD_1 = CardEntity(id = DEFAULT_CARD_ID, label = "Default Card Label")

        private const val SECOND_CARD_ID = 2L
        private val NON_FLIPPED_CARD_2 = CardEntity(id = SECOND_CARD_ID, label = "Second Card Label")

        private const val FLIPPED_CARD_1_ID = 3L
        private val FLIPPED_CARD_1 = CardEntity(id = FLIPPED_CARD_1_ID, label = "Flipped Card Label", isFlipped = true)

        private const val FLIPPED_CARD_2_ID = 4L
        private val FLIPPED_CARD_2 = CardEntity(id = FLIPPED_CARD_2_ID, label = "Flipped Card Label", isFlipped = true)

        private const val DEFAULT_BOARD_ID = 1L
        private const val DEFAULT_BOARD_NAME = "Default Board Name"
        private val DEFAULT_BOARD_CREATE_TIME = System.currentTimeMillis()
        private val DEFAULT_BOARD = BoardEntity(id = DEFAULT_BOARD_ID, name = DEFAULT_BOARD_NAME, createdDatetime = DEFAULT_BOARD_CREATE_TIME)
    }

    private fun populateTestData() {
        val boardId = database.boardDao().create(DEFAULT_BOARD)
        cardDao.create(NON_FLIPPED_CARD_1, boardId)
        cardDao.create(NON_FLIPPED_CARD_2, boardId)
        cardDao.create(FLIPPED_CARD_1, boardId)
        cardDao.create(FLIPPED_CARD_2, boardId)
    }
}
