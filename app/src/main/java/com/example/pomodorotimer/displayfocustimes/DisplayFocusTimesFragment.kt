package com.example.pomodorotimer.displayfocustimes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimer.R
import com.example.pomodorotimer.database.FocusTimeDatabase
import com.example.pomodorotimer.databinding.FragmentDisplayFocusTimesBinding

class DisplayFocusTimesFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding: FragmentDisplayFocusTimesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_display_focus_times, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = FocusTimeDatabase.getInstance(application).focusTimeDatabaseDao

        val displayViewModelFactory = DisplayFocusTimesViewModelFactory(dataSource, application)

        val displayFocusTimerViewModel =
            ViewModelProvider(
                this,displayViewModelFactory).get(DisplayFocusTimesViewModel::class.java)

        val displayFocusTimesAdapter=DisplayFocusTimesAdapter()
        binding.lifecycleOwner=this
        binding.focusTimesRv.adapter=displayFocusTimesAdapter

        displayFocusTimerViewModel.focusTimesList.observe(viewLifecycleOwner, Observer {
            displayFocusTimesAdapter.data = it
        })

        return binding.root
    }
}