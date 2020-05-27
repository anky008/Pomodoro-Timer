package com.example.pomodorotimer.focustimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pomodorotimer.R
import com.example.pomodorotimer.databinding.FragmentTimerBinding
import androidx.lifecycle.ViewModelProvider as ViewModelProvider

class FocusTimerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding:FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false)

        val application = requireNotNull(this.activity).application

        //val dataSource = FocusTimeDatabase.getInstance(application).focusTimeDatabaseDao

        //val viewModelFactory = FocusTimerViewModelFactory(dataSource, application)

        val focusTimerViewModel =
            ViewModelProvider(
                this).get(FocusTimerViewModel::class.java)

        binding.focusTimerViewModel = focusTimerViewModel

        binding.lifecycleOwner = this

        focusTimerViewModel.showSnackBar.observe(viewLifecycleOwner, Observer { showSnckBar ->
            if (showSnckBar){
                Toast.makeText(context,"Wallah you can take a break now",Toast.LENGTH_SHORT).show()
                focusTimerViewModel.doneShowingSnackBar()
            }
        })

        return binding.root
    }
}