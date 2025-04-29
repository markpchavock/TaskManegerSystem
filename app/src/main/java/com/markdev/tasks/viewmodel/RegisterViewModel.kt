package com.markdev.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markdev.tasks.service.constants.TaskConstants
import com.markdev.tasks.service.model.ValidationModel
import com.markdev.tasks.service.repository.PersonRepository
import com.markdev.tasks.service.repository.local.PreferencesManager
import com.markdev.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)
    private val personRepository = PersonRepository(application.applicationContext)


    private val _register = MutableLiveData<ValidationModel>()
    val register: LiveData<ValidationModel> = _register

    fun create(name: String, email: String, password: String) {
        try {
            viewModelScope.launch {
                val response = personRepository.create(name, email, password, "TRUE")
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!

                    preferencesManager.store(TaskConstants.SHARED.TOKEN_KEY, result.token)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_KEY, result.personKey)
                    preferencesManager.store(TaskConstants.SHARED.PERSON_NAME, result.name)

                    RetrofitClient.addHeaders(result.token, result.personKey)

                    super.saveUserAuthenticator(result)
                    _register.value = ValidationModel()
                } else {
                    _register.value = parseErrorMessage(response)
                }
            }
        } catch (e: Exception){
            _register.value = handleException(e)
        }

    }
}