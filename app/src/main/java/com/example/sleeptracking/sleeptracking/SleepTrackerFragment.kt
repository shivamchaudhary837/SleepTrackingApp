package com.example.sleeptracking.sleeptracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sleeptracking.R
import com.example.sleeptracking.database.SleepDatabase
import com.example.sleeptracking.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar


class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.

        val binding:FragmentSleepTrackerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sleep_tracker, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstamce(application).sleepDatabaseDao

        val viewModelFactory = SleepTrackerViewModelFactory(dataSource,application)

        val sleepTrackerViewModel= ViewModelProvider(this,viewModelFactory)
            .get(SleepTrackerViewModel::class.java)

        binding.setLifecycleOwner(this)
        binding.sleepTrackerViewModel=sleepTrackerViewModel

        val adapter=SleepNightAdapter(SleepNightListener{nightId->
          //Toast.makeText(context,"${nightId}",Toast.LENGTH_SHORT).show()
            sleepTrackerViewModel.onSleepNightClicked(nightId)
        })

        binding.sleepList.adapter=adapter

        val manager=GridLayoutManager(activity,3)
        manager.spanSizeLookup=object:GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return when(position){
                    0-> 3
                    else->1
                }
            }

        }
        binding.sleepList.layoutManager=manager

        sleepTrackerViewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer{night->
         night?.let {
             this.findNavController().navigate(
                 SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(night.nightId))
             sleepTrackerViewModel.doneNavigating()
         }
        })

        sleepTrackerViewModel.showSnackBarEvent.observe(viewLifecycleOwner,Observer{
            if(it==true){
                Snackbar.make(requireActivity().findViewById(android.R.id.content),
                getString(R.string.cleared_message),
                Snackbar.LENGTH_SHORT).show()
                sleepTrackerViewModel.doneShowingSnackbar()
            }
        })
        sleepTrackerViewModel.nights.observe(viewLifecycleOwner, Observer {nightList->
            nightList?.let{
                adapter.addHeaderAndSubmitList(nightList)
            }
        })

        sleepTrackerViewModel.navigateToSleepDetail.observe(viewLifecycleOwner, Observer {night->
            night?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections.
                actionSleepTrackerFragmentToSleepDetailFragment(night))
                sleepTrackerViewModel.onSleepDetailNavigated()
            }
        })
        return binding.root

    }


}