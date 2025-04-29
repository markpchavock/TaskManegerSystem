package com.markdev.tasks.service.repository.remote

import com.markdev.tasks.service.model.PriorityModel
import retrofit2.Response
import retrofit2.http.GET


interface PriorityService {

    @GET("Priority")
    suspend fun getList(): Response<List<PriorityModel>>
}
