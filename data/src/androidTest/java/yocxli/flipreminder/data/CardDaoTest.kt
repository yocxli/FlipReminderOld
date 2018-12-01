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
        cardDao.create(DEFAULT_CARD, boardId)

        val result = cardDao.getCardsOf(boardId)

        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(DEFAULT_CARD)
    }

    @Test
    fun getCardsOf_hasTwoOrMoreRecords_returnsAllRecords() {
        createTwoCardInDefaultBoard()
        val flippedCard = CardEntity(label = "Flipped Card Label", isFlipped = true)
        val flippedCardId = cardDao.create(flippedCard, DEFAULT_BOARD_ID)

        val result = cardDao.getCardsOf(DEFAULT_BOARD_ID)

        assertThat(result).hasSize(3)
        assertThat(result[0]).isEqualTo(DEFAULT_CARD)
        assertThat(result[1]).isEqualTo(SECOND_CARD)
        assertThat(result[2]).isEqualTo(flippedCard.copy(id = flippedCardId))
    }

    @Test
    fun getFlippedCardsOf_hasTwoOrMoreRecords_returnsAllRecords() {
        createTwoCardInDefaultBoard()
        val flippedCard1 = CardEntity(label = "Flipped 1", isFlipped = true)
        val flippedCard2 = CardEntity(label = "Flipped 2", isFlipped = true)
        val flippedCard1Id = cardDao.create(flippedCard1, DEFAULT_BOARD_ID)
        val flippedCard2Id = cardDao.create(flippedCard2, DEFAULT_BOARD_ID)

        val result = cardDao.getFlippedCardsOf(DEFAULT_BOARD_ID)

        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(flippedCard1.copy(id = flippedCard1Id))
        assertThat(result[1]).isEqualTo(flippedCard2.copy(id = flippedCard2Id))
    }

    @Test
    fun insertAndFindById_succeedToCreateAndHasARecord_findByIdReturnsIt() {
        cardDao.insert(DEFAULT_CARD)
        val result = cardDao.findById(DEFAULT_CARD_ID)

        assertThat(result).isEqualTo(DEFAULT_CARD)
    }

    @Test
    fun deleteAndFindById_succeedToDeleteAndHasNoRecord_findByIdReturnsNull() {
        cardDao.insert(DEFAULT_CARD)

        cardDao.delete(DEFAULT_CARD)
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
        val duplicateEntity = DEFAULT_CARD.copy(createdDatetime = DEFAULT_CARD.createdDatetime + 1)
        cardDao.insert(DEFAULT_CARD)

        try {
            cardDao.insert(duplicateEntity)
        } catch (e: SQLiteConstraintException) {
            // do nothing
        }

        val result = cardDao.findById(DEFAULT_CARD_ID)
        assertThat(result).isEqualTo(DEFAULT_CARD)
    }

    @Test
    fun update() {
        cardDao.insert(DEFAULT_CARD)
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
        private const val DEFAULT_CARD_LABEL = "Default Card Label"
        private val DEFAULT_CARD = CardEntity(id = DEFAULT_CARD_ID, label = DEFAULT_CARD_LABEL)

        private const val SECOND_CARD_ID = 2L
        private const val SECOND_CARD_LABEL = "Second Card Label"
        private val SECOND_CARD = CardEntity(id = SECOND_CARD_ID, label = SECOND_CARD_LABEL)

        private const val DEFAULT_BOARD_ID = 1L
        private const val DEFAULT_BOARD_NAME = "Default Board Name"
        private val DEFAULT_BOARD_CREATE_TIME = System.currentTimeMillis()
        private val DEFAULT_BOARD = BoardEntity(id = DEFAULT_BOARD_ID, name = DEFAULT_BOARD_NAME, createdDatetime = DEFAULT_BOARD_CREATE_TIME)
    }

    private fun createTwoCardInDefaultBoard() {
        val boardId = database.boardDao().create(DEFAULT_BOARD)
        cardDao.create(DEFAULT_CARD, boardId)
        cardDao.create(SECOND_CARD, boardId)
    }
}
