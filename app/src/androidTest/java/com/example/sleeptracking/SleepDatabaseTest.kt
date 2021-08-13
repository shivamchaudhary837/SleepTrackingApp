package com.example.sleeptracking

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.sleeptracking.database.SleepDatabase
import com.example.sleeptracking.database.SleepDatabaseDao
import com.example.sleeptracking.database.SleepNight
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {
    private lateinit var sleepDao:SleepDatabaseDao
    private lateinit var db:SleepDatabase

    @Before
    fun createDb(){
        val context=InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
     // process is killed.

        db= Room.inMemoryDatabaseBuilder(context,SleepDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()

        sleepDao=db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }

    @Test
    @Throws(IOException::class)
    fun insertAndGetNight(){
        val night=SleepNight()
        sleepDao.insert(night)
        val tonight=sleepDao.getToNight()

        assertEquals(tonight?.sleepQuality,-1)
    }
}