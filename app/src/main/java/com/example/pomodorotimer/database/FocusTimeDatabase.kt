package com.example.pomodorotimer.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities= [FocusTime::class],version = 1,exportSchema = false)
abstract class FocusTimeDatabase : RoomDatabase() {
    abstract val focusTimeDatabaseDao:FocusTimeDatabaseDao

    companion object
    {
        @Volatile
        private var INSTANCE : FocusTimeDatabase?=null

        fun getInstance(context: Context) : FocusTimeDatabase {
            synchronized(this) {

                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FocusTimeDatabase::class.java,
                        "focus_history_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE=instance
                }

                return instance
            }

        }
    }
}