package yocxli.flipreminder.data.board

import androidx.room.*

@Dao
interface BoardDao {
    @Query("SELECT * FROM boards")
    fun listAll(): List<BoardEntity>

    @Query("SELECT * FROM boards WHERE id = :id")
    fun findById(id: Long): BoardEntity?

    @Query("SELECT COUNT(*) FROM boards")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(board: BoardEntity): Long

    @Update
    fun update(board: BoardEntity): Int

    @Delete
    fun delete(board: BoardEntity): Int
}