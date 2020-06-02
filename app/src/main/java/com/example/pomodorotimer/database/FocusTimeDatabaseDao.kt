package com.example.pomodorotimer.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface FocusTimeDatabaseDao
{
    @Insert
    fun insert(time:FocusTime)

    @Update
    fun update(night:FocusTime)

    @Query("SELECT * from focus_time_table where focusTimeId=:key")
    fun get(key: Long):FocusTime?

    @Query("DELETE FROM focus_time_table")
    fun clear()

    @Query("SELECT * FROM focus_time_table ORDER BY focusTimeId DESC")
    fun getAllFocusTimes(): LiveData<List<FocusTime>>

    @Query("SELECT * FROM focus_time_table ORDER BY focusTimeId DESC LIMIT 1")
    fun getCurrentFocusTime():FocusTime

}
