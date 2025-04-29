package com.markdev.tasks.service.repository.remote

import com.markdev.tasks.service.model.TaskModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskService {

    @GET("Task")
    suspend fun listAllTask(): Response<List<TaskModel>>

    @GET("Task/Next7Days")
    suspend fun listNextSevenDays(): Response<List<TaskModel>>

    @GET("Task/Overdue")
    suspend fun listOverDue(): Response<List<TaskModel>>

    @GET("Task/{id}")
    suspend fun getTask(
        @Path(value = "id", encoded = true) id: Int
    ): Response<TaskModel>

    @POST("Task")
    @FormUrlEncoded
    suspend fun insertTask(
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean,
    ): Response<Boolean>

    @PUT("Task")
    @FormUrlEncoded
    suspend fun updateTask(
        @Field("Id") id: Int,
        @Field("PriorityId") priorityId: Int,
        @Field("Description") description: String,
        @Field("DueDate") dueDate: String,
        @Field("Complete") complete: Boolean,
    ): Response<Boolean>

    @PUT("Task/Complete")
    @FormUrlEncoded
    suspend fun completeTask(@Field ("Id") id: Int): Response<Boolean>

    @PUT("Task/Undo")
    @FormUrlEncoded
    suspend fun undoTask(@Field ("Id") id: Int): Response<Boolean>

    @HTTP(method = "DELETE", path = "Task", hasBody = true)
    @FormUrlEncoded
    suspend fun deleteTask(@Field("Id") id: Int): Response<Boolean>
}
