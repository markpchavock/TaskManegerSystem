package com.markdev.tasks.service.repository

import android.content.Context
import com.markdev.tasks.service.model.TaskModel
import com.markdev.tasks.service.repository.remote.RetrofitClient
import com.markdev.tasks.service.repository.remote.TaskService
import retrofit2.Response

class TaskRepository(context: Context) : BaseRepository(context) {

    private val remote = RetrofitClient.getService(TaskService::class.java)

    suspend fun save (task: TaskModel): Response<Boolean> {
           return  safeApiCall { remote.insertTask(task.priorityId,task.description,task.dueDate,task.complete) }
    }

    suspend fun update (task: TaskModel): Response<Boolean> {
        return  safeApiCall { remote.updateTask(task.id,task.priorityId,task.description,task.dueDate,task.complete) }
    }

    suspend fun load(id: Int) : Response<TaskModel> {
        return safeApiCall { remote.getTask(id) }
    }

    suspend fun complete(id: Int): Response<Boolean> {
        return safeApiCall { remote.completeTask(id) }
    }

    suspend fun undo(id: Int): Response<Boolean> {
        return safeApiCall { remote.undoTask(id) }
    }

    suspend fun list() : Response<List<TaskModel>> {
        return safeApiCall { remote.listAllTask() }
    }

    suspend fun listNextSevenDays() : Response<List<TaskModel>> {
        return safeApiCall { remote.listNextSevenDays() }
    }

    suspend fun listOverdue() : Response<List<TaskModel>> {
        return safeApiCall { remote.listOverDue() }
    }

    suspend fun deleteTask(id: Int): Response<Boolean> {
        return safeApiCall { remote.deleteTask(id) }
    }
}