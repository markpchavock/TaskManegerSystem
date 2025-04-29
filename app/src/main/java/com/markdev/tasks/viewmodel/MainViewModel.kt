package com.markdev.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markdev.tasks.service.constants.TaskConstants
import com.markdev.tasks.service.repository.local.PreferencesManager
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)

    private val _userName = MutableLiveData<String>()
     val userName: LiveData<String> = _userName

    fun logout() {
        viewModelScope.launch {

            preferencesManager.remove(TaskConstants.SHARED.PERSON_KEY)
            preferencesManager.remove(TaskConstants.SHARED.TOKEN_KEY)
            preferencesManager.remove(TaskConstants.SHARED.PERSON_NAME)
        }
    }

    fun loadUserName(){
        viewModelScope.launch {
            _userName.value = preferencesManager.get(TaskConstants.SHARED.PERSON_NAME)
        }
    }
}