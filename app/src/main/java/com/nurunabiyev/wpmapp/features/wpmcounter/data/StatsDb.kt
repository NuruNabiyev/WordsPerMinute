package com.nurunabiyev.wpmapp.features.wpmcounter.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nurunabiyev.wpmapp.features.wpmcounter.domain.Stats

@Entity(tableName = "statistics")
data class StatsDb(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "wpm") val wpm: Int = 0,
    @ColumnInfo(name = "wordCharacterAccuracy") val wordCharacterAccuracy: Int = 0,
    @ColumnInfo(name = "wpmWithAccuracy") val wpmWithAccuracy: Int = 0
)

fun Stats.toStatsDb() = StatsDb(
    wpm = wpm,
    wordCharacterAccuracy = wordCharacterAccuracy,
    wpmWithAccuracy = wpmWithAccuracy
)