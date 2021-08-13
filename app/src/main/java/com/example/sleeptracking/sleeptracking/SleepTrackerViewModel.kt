package com.example.sleeptracking.sleeptracking

import android.app.Application
import androidx.lifecycle.*

import com.example.sleeptracking.database.SleepDatabaseDao
import com.example.sleeptracking.database.SleepNight


import com.example.sleeptracking.formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    val database: SleepDatabaseDao,
    application: Application
):AndroidViewModel(application) {

     val nights=database.getAllNight()
    val nightString=Transformations.map(nights){nights->
        formatNights(nights,application.resources)
    }

    private var tonight = MutableLiveData<SleepNight?>()

    private val _navigateToSleepQuality=MutableLiveData<SleepNight>()

    val navigateToSleepQuality:LiveData<SleepNight>
    get() = _navigateToSleepQuality

    val startButtonVisible = Transformations.map(tonight){
        it==null
    }
    val stopButtonVisible= Transformations.map(tonight){
        it!=null
    }
    val clearButtonVisible= Transformations.map(nights){
        it?.isNotEmpty()
    }

   
    private var _showSnackBarEvent=MutableLiveData<Boolean>()
    val showSnackBarEvent:LiveData<Boolean>
    get() = _showSnackBarEvent


    private val _navigateToSleepDetail=MutableLiveData<Long>()
    val navigateToSleepDetail
    get() = _navigateToSleepDetail

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
          tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
          var night=database.getToNight()

        if(night?.endTimeMillis != night?.startTimeMilli){
            night=null
        }

        return night
    }

    fun onStartTracking(){
        viewModelScope.launch {

            val newNight=SleepNight()
            insert(newNight)
            tonight.value=getTonightFromDatabase()

        }
    }

    fun onStopTracking(){
        viewModelScope.launch {
            // In Kotlin, the return@launch syntax is used for specifying which function among
            // several nested ones this statement returns from.
            // In this case, we are specifying to return from launch(),
            // not the lambda.
            val oldNight=tonight.value?:return@launch
            oldNight.endTimeMillis=System.currentTimeMillis()
            update(oldNight)

            _navigateToSleepQuality.value=oldNight
        }
    }

    fun onClear(){
        viewModelScope.launch {
            clear()
            tonight.value=null
            _showSnackBarEvent.value=true
        }
    }

    private suspend fun insert(night:SleepNight){
      database.insert(night)
    }

    private suspend fun update(night:SleepNight){
        database.update(night)
    }
    private suspend fun clear(){
        database.clear()
    }

    fun doneNavigating(){
         _navigateToSleepQuality.value=null
    }

    fun doneShowingSnackbar(){
        _showSnackBarEvent.value=false
    }

    fun onSleepNightClicked(id:Long){
        _navigateToSleepDetail.value=id
    }

    fun onSleepDetailNavigated(){
        _navigateToSleepDetail.value=null
    }
}