package com.example.pomodorotimer.displayfocustimes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pomodorotimer.database.FocusTime
import com.example.pomodorotimer.database.FocusTimeDatabaseDao

class DisplayFocusTimesViewModel(val database: FocusTimeDatabaseDao, application: Application) :
    AndroidViewModel(application){

    private var _focusTimesList=database.getAllFocusTimes()
    val focusTimesList:LiveData<List<FocusTime>>
    get()=_focusTimesList

}