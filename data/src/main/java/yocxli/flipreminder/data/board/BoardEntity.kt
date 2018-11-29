package yocxli.flipreminder.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "boards", indices = [Index(value = arrayOf("name"), unique = true)])
data class BoardEntity (
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "created_datetime") var createdDatetime: Long = System.currentTimeMillis()
)