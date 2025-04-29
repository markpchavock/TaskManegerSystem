package com.markdev.tasks.service.repository

import android.content.Context
import com.markdev.tasks.service.model.PersonModel
import com.markdev.tasks.service.repository.remote.PersonService
import com.markdev.tasks.service.repository.remote.RetrofitClient
import retrofit2.Response

class PersonRepository(val context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(PersonService::class.java)

    suspend fun login(email: String, password: String): Response<PersonModel> {

        return safeApiCall { remote.login(email, password) }
    }

    suspend fun create(
        name: String,
        email: String,
        password: String,
        receiveNews: String
    ): Response<PersonModel> {

        return safeApiCall { remote.create(name, email, password, receiveNews) }
    }

    }
