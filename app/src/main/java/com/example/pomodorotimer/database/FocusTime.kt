package com.example.pomodorotimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "focus_time_table")
data class FocusTime(

    @PrimaryKey(autoGenerate = true)
    var focusTimeId:Long=0L,

    @ColumnInfo(name = "start_time_milli")
    var startTimeMilli:Long=System.currentTimeMillis(),

    @ColumnInfo(name="end_time_milli")
    var endTimeMilli:Long=startTimeMilli,

    @ColumnInfo(name="is_complete")
    var isComplete:Boolean=false
)