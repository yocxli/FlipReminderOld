package yocxli.flipreminder.domain

interface BoardRepository {
    fun getBoards(): List<Board>

    fun findBoard(): Board
    fun renameBoard(board: Board): Unit
    fun getCardsOf(board: Board)
}