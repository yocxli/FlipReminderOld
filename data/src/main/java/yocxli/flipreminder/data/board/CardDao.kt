package yocxli.flipreminder.data.board

import androidx.room.*

@Dao
interface CardDao {
    @Query("SELECT * FROM cards INNER JOIN card_relations ON card_relations.card_id = cards.id WHERE card_relations.board_id = :boardId")
    fun getCardsOf(boardId: Long): List<CardEntity>

    @Query("SELECT * FROM cards INNER JOIN card_relations ON card_relations.card_id = cards.id WHERE card_relations.board_id = :boardId AND flipped = 1")
    fun getFlippedCardsOf(boardId: Long): List<CardEntity>

    @Query("SELECT * FROM cards WHERE flipped = 1")
    fun getFlippedCards(): List<CardEntity>

    @Transaction
    fun create(card: CardEntity, boardId: Long): Long {
        val cardId = insert(card)
        addToBoard(CardRelationEntity(boardId = boardId, cardId = cardId))
        return cardId
    }


    @Query("SELECT * FROM cards WHERE id = :id")
    fun findById(id: Long): CardEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(card: CardEntity): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addToBoard(cardRelationEntity: CardRelationEntity)

    @Update
    fun update(card: CardEntity): Int

    @Delete
    fun delete(card: CardEntity): Int
}