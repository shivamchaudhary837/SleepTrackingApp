package com.example.sleeptracking.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sleeptracking.database.SleepDatabaseDao
import com.example.sleeptracking.database.SleepNight

class SleepDetailViewModel (
    private val sleepNightKey:Long=0L,
    dataSource:SleepDatabaseDao): ViewModel(){

    val database=dataSource

    private val night:LiveData<SleepNight>
    init {
        night=database.getNightWithId(sleepNightKey)
    }
    fun getNight()=night

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()

    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker


    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }

    fun onClose() {
        _navigateToSleepTracker.value = true
    }
}