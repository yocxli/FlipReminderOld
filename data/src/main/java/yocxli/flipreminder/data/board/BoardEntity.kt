package yocxli.flipreminder.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boards")
data class BoardEntity (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_datetime") var createdDatetime: Long = System.currentTimeMillis()
)