package com.example.pomodorotimer.displayfocustimes

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pomodorotimer.database.FocusTime
import com.example.pomodorotimer.database.FocusTimeDatabaseDao
import com.example.pomodorotimer.focustimer.FocusTimerViewModel
import kotlinx.coroutines.*

class DisplayFocusTimesViewModel(val database: FocusTimeDatabaseDao, application: Application) :
    AndroidViewModel(application){

    private var _focusTimesList=database.getAllFocusTimes()
    val focusTimesList:LiveData<List<FocusTime>>
    get()=_focusTimesList

    private val viewModelJob= Job()
    private val uiScope= CoroutineScope(Dispatchers.Main + viewModelJob)

    val hideCancelButton=Transformations.map(focusTimesList) {
        if (it.isNotEmpty()){ View.VISIBLE }
        else{ View.INVISIBLE }
    }

    fun clearAll(){
        Log.i("DisplayFocusTimes","Clear all button clicked!!")
        uiScope.launch {
            deleteAll()
        }
    }

    private suspend fun deleteAll() {
        return withContext(Dispatchers.IO){
            database.clear()
        }
    }
}