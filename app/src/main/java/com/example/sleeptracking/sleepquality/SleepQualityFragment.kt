package com.example.sleeptracking.sleepquality

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.sleeptracking.R
import com.example.sleeptracking.database.SleepDatabase
import com.example.sleeptracking.databinding.FragmentSleepQualityBinding



class SleepQualityFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding:FragmentSleepQualityBinding= DataBindingUtil.
        inflate(inflater,R.layout.fragment_sleep_quality,container,false)
        val application= requireNotNull(this.activity).application

        val arguments=SleepQualityFragmentArgs.fromBundle(requireArguments())
        val dataSource=SleepDatabase.getInstamce(application).sleepDatabaseDao

        val viewModelFactory=SleepQualityViewModelFactory(arguments.sleepNightKey,dataSource)
        val sleepQualityViewModel= ViewModelProvider(this,viewModelFactory)
            .get(SleepQualityViewModel::class.java)

        binding.sleepQualityViewModel=sleepQualityViewModel

        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if(it==true){
                this.findNavController().navigate(
                    SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment()
                )
                sleepQualityViewModel.doneNavigating()
            }
        })
        return binding.root
    }


}