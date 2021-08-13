package com.example.sleeptracking.sleepdetail

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
import com.example.sleeptracking.databinding.FragmentSleepDetailBinding
import com.example.sleeptracking.sleeptracking.SleepTrackerFragmentDirections


class SleepDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        val binding:FragmentSleepDetailBinding= DataBindingUtil.
        inflate(inflater,R.layout.fragment_sleep_detail,container,false)

        val application= requireNotNull(this.activity).application
        val argument=SleepDetailFragmentArgs.fromBundle(requireArguments())

        val dataSource=SleepDatabase.getInstamce(application).sleepDatabaseDao
        val viewModelFactory=SleepDetailViewModelFactory(argument.sleepNightKey,dataSource)

        val sleepDetailViewModel=ViewModelProvider(this,viewModelFactory).
        get(SleepDetailViewModel::class.java)

        binding.sleepDetailViewModel=sleepDetailViewModel
        //important to set setLifecycleOwner
        binding.setLifecycleOwner(this)

         sleepDetailViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
             if(it==true){

                 this.findNavController().navigate(
                     SleepDetailFragmentDirections.actionSleepDetailFragmentToSleepTrackerFragment()
                 )
                 sleepDetailViewModel.doneNavigating()
             }
         })

        return   binding.root
    }


}