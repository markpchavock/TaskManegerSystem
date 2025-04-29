package com.markdev.tasks.service.model

class ValidationModel(message: String = "") {
    private var status: Boolean = true
    private var errorMessage:  String = ""

    init {
        if (message != ""){
            status = false
            errorMessage = message
        }
    }

    fun status() = status
    fun message() = errorMessage
}