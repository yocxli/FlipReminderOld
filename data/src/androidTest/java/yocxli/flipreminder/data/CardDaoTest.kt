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
        val result = cardDao.getCardsOf(1)

        assertThat(result).hasSize(0)
    }

    @Test
    fun getCardsOf_hasOneRecord_returnsOneRecord() {
//        cardDao.insert(DEFAULT_CARD)

//        val result = database.boardDao().listAll()
//
//        assertThat(result).hasSize(1)
//        assertThat(result[0]).isEqualTo(DEFAULT_BOARD)
    }

    @Test
    fun getCardOf_hasTwoOrMoreRecords_returnsAllRecords() {
//        database.boardDao().insert(DEFAULT_BOARD)
//        val secondBoard = BoardEntity(id = 2, name = "Second Board")
//        database.boardDao().insert(secondBoard)
//
//        val result = database.boardDao().listAll()
//
//        assertThat(result).hasSize(2)
//        assertThat(result[0]).isEqualTo(DEFAULT_BOARD)
//        assertThat(result[1]).isEqualTo(secondBoard)
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
    }
}
