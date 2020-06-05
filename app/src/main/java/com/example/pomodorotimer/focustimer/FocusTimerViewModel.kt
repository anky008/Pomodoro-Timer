package com.example.pomodorotimer.focustimer

import android.app.Application
import android.media.MediaPlayer
import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.pomodorotimer.database.FocusTime
import com.example.pomodorotimer.database.FocusTimeDatabaseDao
import kotlinx.coroutines.*

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class FocusTimerViewModel(val database: FocusTimeDatabaseDao,
                          application: Application) : AndroidViewModel(application) {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }


    private var timer: CountDownTimer?
    private var _currentTime = MutableLiveData<Long>()
    private val currentTime: LiveData<Long>
        get() = _currentTime

    private var mediaPlayer:MediaPlayer? = null

    private val _buzzType = MutableLiveData<BuzzType>()
    val buzzType: LiveData<BuzzType>
        get() = _buzzType

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val showStartButton = Transformations.map(_currentTime) {
        it == START_TIME
    }

    val showCancelButton = Transformations.map(_currentTime) {
        it != START_TIME
    }

    val hideStartButton = Transformations.map(showStartButton) {
        if (it == true) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    val hideCancelButton = Transformations.map(showCancelButton) {
        if (it == true) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    private val _showSnackBar = MutableLiveData<Boolean>()
    val showSnackBar: LiveData<Boolean>
        get() = _showSnackBar

    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    init {
        timer = null
        resetTimer()
        Log.i("FocusTimerViewModel", "View model initialized!!!")
        Log.i("FocusTimerViewModel", _currentTime.value.toString())
        _showSnackBar.value = false
    }

    companion object {
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 1500000L
        const val START_TIME = 1500L
    }

    fun resetTimer() {
        _currentTime.value = START_TIME
    }

    fun onTimerStart() {
        uiScope.launch {
            startTimer()
            val newFocusTime = FocusTime()
            insert(newFocusTime)
        }
    }

    fun onTimerCancel() {
        uiScope.launch {
            //val oldFocusTime=currFocusTime.value ?: return@launch
            val oldFocusTime: FocusTime = getCurrentFocusTime()
            oldFocusTime.endTimeMilli = System.currentTimeMillis()
            Log.i("FocusTimerViewModel", "Timer cancelled")
            update(oldFocusTime)
        }

        _buzzType.value = BuzzType.COUNTDOWN_PANIC
        cancelTimer()
        resetTimer()
    }

    private suspend fun getCurrentFocusTime(): FocusTime {
        lateinit var focusTime: FocusTime
        withContext(Dispatchers.IO) {
            focusTime = database.getCurrentFocusTime()
        }
        return focusTime
    }

    private suspend fun insert(focusTime: FocusTime) {
        withContext(Dispatchers.IO) {
            database.insert(focusTime)
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                Log.i("FocusTimerViewModel", _currentTime.value.toString())
            }

            override fun onFinish() {
                onTimerStop()
            }
        }

        (timer as CountDownTimer).start()
    }

    private fun onTimerStop() {
        _showSnackBar.value = true
        uiScope.launch {
            val oldFocusTime = getCurrentFocusTime()
            oldFocusTime.endTimeMilli = System.currentTimeMillis()
            Log.i("FocusTimerViewModel", "Timer stops!!")
            update(oldFocusTime)

            _buzzType.value = BuzzType.CORRECT
        }
    }

    private suspend fun update(focusTime: FocusTime) {
        Log.i("FocusViewModel", "Update funcn called!!")
        withContext(Dispatchers.IO) {
            database.update(focusTime)
        }
    }

    fun doneShowingSnackBar() {
        _showSnackBar.value = false
        resetTimer()
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer()
        viewModelJob.cancel()
    }

    fun cancelTimer() {
        timer?.cancel()
    }

    fun onBuzzComplete() {
        _buzzType.value = BuzzType.NO_BUZZ
    }

    fun clearDatabase() {
        uiScope.launch {
            cancelTimer()
            resetTimer()
            deleteAll()
        }
    }

    private suspend fun deleteAll() {
        return withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    private fun playMusic(){

    }
}