package com.markdev.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.markdev.tasks.service.constants.TaskConstants
import com.markdev.tasks.service.model.TaskModel
import com.markdev.tasks.service.model.ValidationModel
import com.markdev.tasks.service.repository.PriorityRepository
import com.markdev.tasks.service.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(application: Application) : BaseAndroidViewModel(application) {
    private val taskRepository = TaskRepository(application.applicationContext)
    private val priorityRepository = PriorityRepository(application.applicationContext)
    private var taskfilter = TaskConstants.FILTER.ALL


    private val _tasks = MutableLiveData<List<TaskModel>>()
    val tasks: LiveData<List<TaskModel>> = _tasks

    private val _taskDeleted = MutableLiveData<ValidationModel>()
    val taskDeleted: LiveData<ValidationModel> = _taskDeleted

    fun list(filter: Int) {
        taskfilter = filter
        viewModelScope.launch {
            val response = when (filter) {
                TaskConstants.FILTER.ALL -> taskRepository.list()
                TaskConstants.FILTER.NEXT -> taskRepository.listNextSevenDays()
                else -> taskRepository.listOverdue()
            }
            if (response.isSuccessful && response.body() != null) {
                val result = response.body()!!

                result.map { task ->
                    task.priorityDescription = priorityRepository.getDescription(task.priorityId)
                }
                _tasks.value = result

            }
        }

    }

    fun status(taskId: Int, complete: Boolean) {
        viewModelScope.launch {
            val response = if (complete) {
                taskRepository.complete(taskId)
            } else {
                taskRepository.undo(taskId)
            }
            if (response.isSuccessful && response.body() != null) {
                list(taskfilter)
            }
        }

    }

    fun delete(taskId: Int) {
        viewModelScope.launch {
            try {
                val response = taskRepository.deleteTask(taskId)
                if (response.isSuccessful && response.body() != null) {
                    list(taskfilter)
                    _taskDeleted.value = ValidationModel()
                }
            } catch (e: Exception){
                _taskDeleted.value = handleException(e)
            }
        }
    }
}