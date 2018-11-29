package yocxli.flipreminder.data.board

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(BoardEntity::class, CardEntity::class), version = 1)
abstract class BoardDatabase : RoomDatabase() {
    abstract fun boardDao(): BoardDao
    abstract fun cardDao(): CardDao

    companion object {
        private var INSTANCE: BoardDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): BoardDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        BoardDatabase::class.java,
                        "board.db"
                    ).build()
                }
                return INSTANCE!!
            }
        }

    }
}