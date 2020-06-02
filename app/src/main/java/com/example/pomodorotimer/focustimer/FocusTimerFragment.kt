package com.example.pomodorotimer.focustimer

import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.pomodorotimer.R
import com.example.pomodorotimer.database.FocusTimeDatabase
import com.example.pomodorotimer.database.FocusTimerViewModelFactory
import com.example.pomodorotimer.databinding.FragmentTimerBinding
import androidx.lifecycle.ViewModelProvider as ViewModelProvider

class FocusTimerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding:FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = FocusTimeDatabase.getInstance(application).focusTimeDatabaseDao

        val viewModelFactory = FocusTimerViewModelFactory(dataSource, application)

        val focusTimerViewModel =
            ViewModelProvider(
                this,viewModelFactory).get(FocusTimerViewModel::class.java)

        binding.focusTimerViewModel = focusTimerViewModel

        binding.lifecycleOwner = this

        focusTimerViewModel.buzzType.observe(viewLifecycleOwner, Observer { buzzType->
          if (buzzType!=FocusTimerViewModel.BuzzType.NO_BUZZ){
              buzz(buzzType.pattern)
              focusTimerViewModel.onBuzzComplete()
          }
        })

        focusTimerViewModel.showSnackBar.observe(viewLifecycleOwner, Observer { showSnckBar ->
            if (showSnckBar){
                Toast.makeText(context,"Wallah you can take a break now",Toast.LENGTH_SHORT).show()
                focusTimerViewModel.doneShowingSnackBar()
            }
        })

        return binding.root
    }

    private fun buzz(pattern: LongArray) {
        val buzzer = activity?.getSystemService<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }

}