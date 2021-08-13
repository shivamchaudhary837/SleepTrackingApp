package com.example.sleeptracking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SleepNight::class],version = 1,exportSchema = false)
abstract class SleepDatabase():RoomDatabase() {

abstract val sleepDatabaseDao:SleepDatabaseDao

    companion object{
//        Annotate INSTANCE with @Volatile. The value of a volatile
//        variable will never be cached, and all writes and reads will
//        be done to and from the main memory. This helps make sure the
//        value of INSTANCE is always up-to-date and the same to all execution
//        threads. It means that changes made by one thread to INSTANCE are
//        visible to all other threads immediately, and you don'tget a situation
//        where, say, two threads each update the same entity in a cache, which
//        would create a problem.
        @Volatile
     private var INSTANCE:SleepDatabase? =null
        fun getInstamce(context: Context):SleepDatabase{

            synchronized(this){
                var instance= INSTANCE
                if(instance==null){
                    instance= Room.databaseBuilder(
                        context,
                        SleepDatabase::class.java,
                        "sleep_history_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE=instance
                }
                return instance
            }
        }
    }
}