package com.example.pomodorotimer.focustimer

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.pomodorotimer.database.FocusTime
import com.example.pomodorotimer.database.FocusTimeDatabaseDao
import kotlinx.coroutines.*

class FocusTimerViewModel : ViewModel(){

    private lateinit var timer:CountDownTimer
    private var _currentTime = MutableLiveData<Long>()
    private val currentTime : LiveData<Long>
        get() = _currentTime

    val showStartButton=Transformations.map(_currentTime){
        it == START_TIME
    }

    val showCancelButton=Transformations.map(_currentTime){
        it != START_TIME
    }

    val hideStartButton=Transformations.map(showStartButton) {
        if (it==true){
            View.VISIBLE
        }

        else{
            View.INVISIBLE
        }
    }

    val hideCancelButton=Transformations.map(showCancelButton){
        if (it==true){
            View.VISIBLE
        }
        else{
            View.INVISIBLE
        }
    }

    private val _showSnackBar = MutableLiveData<Boolean> ()
    val showSnackBar:LiveData<Boolean>
    get()=_showSnackBar

    val currentTimeString = Transformations.map(currentTime){time->
            DateUtils.formatElapsedTime(time)
    }

    init {
        resetTimer()
        Log.i("FocusTimerViewModel","View model initialized!!!")
        Log.i("FocusTimerViewModel", _currentTime.value.toString())
        _showSnackBar.value = false }

    companion object {
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60000L
        private const val START_TIME=60L
    }

    fun onTimerStart() {
        Log.i("Clicks","Button Clicked!!!")
        startTimer()
    }



    fun onTimerCancel() {
        timer.cancel()
        resetTimer()
    }

    private fun startTimer(){
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                Log.i("FocusTimerViewModel",_currentTime.value.toString())
            }
            override fun onFinish() {
                onTimerStop()
            }
        }

        timer.start()
    }

    private fun onTimerStop() {
        _showSnackBar.value=true
    }

    fun doneShowingSnackBar(){
        _showSnackBar.value = false
        resetTimer()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()


    }

    private fun resetTimer() {
        _currentTime.value = START_TIME
    }
}