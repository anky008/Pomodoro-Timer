package com.example.pomodorotimer.focustimer

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimer.database.FocusTimeDatabaseDao

class FocusTimerViewModelFactory(
    private val dataSource: FocusTimeDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FocusTimerViewModel::class.java)) {
            return FocusTimerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}