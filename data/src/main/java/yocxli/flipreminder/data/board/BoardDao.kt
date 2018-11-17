package yocxli.flipreminder.data.board

import androidx.room.*

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards WHERE id = :id")
    fun findById(id: Int): BoardEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(board: BoardEntity)

    @Update
    fun update(board: BoardEntity)
}