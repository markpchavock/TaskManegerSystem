package com.markdev.tasks.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markdev.tasks.service.constants.TaskConstants
import com.markdev.tasks.service.helper.BiometricHelper
import com.markdev.tasks.service.model.ValidationModel
import com.markdev.tasks.service.repository.PersonRepository
import com.markdev.tasks.service.repository.PriorityRepository
import com.markdev.tasks.service.repository.local.PreferencesManager
import com.markdev.tasks.service.repository.remote.RetrofitClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val preferencesManager = PreferencesManager(application.applicationContext)
    private val personRepository = PersonRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)

    private val _login = MutableLiveData<ValidationModel>()
    val login: LiveData<ValidationModel> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser: LiveData<Boolean> = _loggedUser

    fun login(email: String, password: String) {

        viewModelScope.launch {
           try{
               val response = personRepository.login(email, password)
               if (response.isSuccessful && response.body() != null) {

                   val personModel = response.body()!!

                   RetrofitClient.addHeaders(personModel.token,personModel.personKey)
                   super.saveUserAuthenticator(personModel)
                   _login.value = ValidationModel()

               } else {
                   _login.value = parseErrorMessage(response)
               }
           } catch (e: Exception){
                _login.value = handleException(e)
           }
        }

    }

     fun verifyAuthentication(){
        viewModelScope.launch {
            val token = preferencesManager.get(TaskConstants.SHARED.TOKEN_KEY)
            val personKey = preferencesManager.get(TaskConstants.SHARED.PERSON_KEY)

            RetrofitClient.addHeaders(token,personKey)

            val logged = (token != "" && personKey != "")
            _loggedUser.value = logged && BiometricHelper.isBiometricAvailable(getApplication())

            if(!logged){
               try {
                   val response = priorityRepository.getList()
                   if (response.isSuccessful && response.body() != null){
                       priorityRepository.saveList(response.body()!!)
                   }
               } catch (e: Exception){
                    Log.e("LoginViewModel",e.message.toString())
               }
            }
        }
    }
}