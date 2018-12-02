package yocxli.flipreminder.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "label") var label: String,
    @ColumnInfo(name = "created_datetime") var createdDatetime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "modified_datetime") var modifiedDatetime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "flipped") var isFlipped: Boolean = false
)