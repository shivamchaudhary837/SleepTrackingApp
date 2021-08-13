package com.example.sleeptracking.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sleeptracking.database.SleepDatabaseDao
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

class SleepQualityViewModelFactory(
    private val sleepNightKey:Long,
    private val dataSource:SleepDatabaseDao
) :ViewModelProvider.Factory{

    @Suppress("uncheked cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(SleepQualityViewModel::class.java)){
            return SleepQualityViewModel(sleepNightKey,dataSource) as T
        }
        throw  IllegalArgumentException("unknown viewmodel class")
    }


}