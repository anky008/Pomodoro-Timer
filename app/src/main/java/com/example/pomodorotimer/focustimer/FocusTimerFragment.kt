package com.example.pomodorotimer.focustimer

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.pomodorotimer.R
import com.example.pomodorotimer.database.FocusTimeDatabase
import com.example.pomodorotimer.databinding.FragmentTimerBinding
import androidx.lifecycle.ViewModelProvider as ViewModelProvider

class FocusTimerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val binding:FragmentTimerBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_timer, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = FocusTimeDatabase.getInstance(application).focusTimeDatabaseDao

        val viewModelFactory =
            FocusTimerViewModelFactory(
                dataSource,
                application
            )

        val focusTimerViewModel =
            ViewModelProvider(
                this,viewModelFactory).get(FocusTimerViewModel::class.java)

        binding.focusTimerViewModel = focusTimerViewModel

        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        focusTimerViewModel.buzzType.observe(viewLifecycleOwner, Observer { buzzType->
          if (buzzType!=FocusTimerViewModel.BuzzType.NO_BUZZ){
             // buzz(buzzType.pattern)
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

    /*private fun buzz(pattern: LongArray) {
        val buzzer = activity?.get<Vibrator>()

        buzzer?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                buzzer.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                //deprecated in API 26
                buzzer.vibrate(pattern, -1)
            }
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       super.onCreateOptionsMenu(menu,inflater)
        inflater.inflate(R.menu.menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.focus_streaks_menu_item -> navigate()
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun navigate() {
        Log.i("FocusTimerFragment","Yes ")
        view?.findNavController()?.navigate(R.id.action_focusTimerFragment_to_focusTimesDisplayFragment)
    }
}