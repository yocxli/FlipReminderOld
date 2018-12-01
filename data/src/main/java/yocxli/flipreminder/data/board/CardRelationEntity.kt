package yocxli.flipreminder.data.board

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "card_relations",
    foreignKeys = [
        ForeignKey(entity = BoardEntity::class, parentColumns = ["id"], childColumns = ["board_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CardEntity::class, parentColumns = ["id"], childColumns = ["card_id"], onDelete = ForeignKey.CASCADE)
    ])
data class CardRelationEntity(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    @ColumnInfo(name = "board_id") var boardId: Long,
    @ColumnInfo(name = "card_id") var cardId: Long
)