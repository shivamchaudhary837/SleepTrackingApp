package com.example.sleeptracking.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {


    @Insert
    suspend fun insert(night:SleepNight)

    @Update
    suspend  fun update(night:SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId= :key")
    suspend  fun get(key:Long):SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend  fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId  DESC LIMIT 1")
    suspend  fun getToNight():SleepNight?

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNight():LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId=:key ")
    fun getNightWithId(key:Long):LiveData<SleepNight>
}