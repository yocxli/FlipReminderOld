package yocxli.flipreminder.data.board

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao interface CardDao {
    @Query("SELECT * FROM cards")
    fun getCards(): List<CardEntity>

    @Query("SELECT * FROM cards WHERE id = :id")
    fun getCardById(id: Int): CardEntity?

    @Query("SELECT * FROM cards WHERE flipped = 1")
    fun getFlippedCards(): List<CardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(card: CardEntity) : Long

    @Query("DELETE FROM cards")
    fun deleteAll()
}