package com.markdev.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.markdev.tasks.R
import com.markdev.tasks.service.constants.TaskConstants
import com.markdev.tasks.service.exception.NoInternetException
import com.markdev.tasks.service.model.PersonModel
import com.markdev.tasks.service.model.ValidationModel
import com.markdev.tasks.service.repository.local.PreferencesManager
import com.google.gson.Gson
import retrofit2.Response

open class BaseAndroidViewModel(private val application: Application) : AndroidViewModel(application) {

    private val preferenceManager = PreferencesManager(application.applicationContext)

    suspend fun saveUserAuthenticator(personModel: PersonModel) {
        preferenceManager.store(TaskConstants.SHARED.PERSON_KEY, personModel.personKey)
        preferenceManager.store(TaskConstants.SHARED.TOKEN_KEY, personModel.token)
        preferenceManager.store(TaskConstants.SHARED.PERSON_NAME, personModel.name)
    }

    fun <T> parseErrorMessage(response: Response<T>): ValidationModel {
        return ValidationModel(
            Gson().fromJson(
                response.errorBody()?.string().toString(),
                String::class.java
            )
        )
    }

    fun handleException (e: Exception) : ValidationModel {
        return if( e is NoInternetException){
             ValidationModel(e.errorMessage)
        } else {
             ValidationModel(application.applicationContext.getString(R.string.error_unexpected))
        }
    }

}