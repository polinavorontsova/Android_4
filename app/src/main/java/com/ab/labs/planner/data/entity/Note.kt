package com.ab.labs.planner.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @ColumnInfo(name = "user_id") var userId: Long,
    @ColumnInfo(name = "tittle") var tittle: String,
    @ColumnInfo(name = "text") var text: String,
    @ColumnInfo(name = "datetime") val datetime: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0

    @ColumnInfo(name = "icon")
    var icon: String = ""
}
