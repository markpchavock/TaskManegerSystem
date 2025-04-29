package com.markdev.tasks.service.constants

class TaskConstants private constructor() {


    object SHARED {
        const val TOKEN_KEY = "tokenkey"
        const val PERSON_KEY = "personkey"
        const val PERSON_NAME = "personname"
    }


    object HEADER {
        const val TOKEN_KEY = "token"
        const val PERSON_KEY = "personkey"
    }

    object BUNDLE {
        const val TASKID = "taskid"
        const val TASKFILTER = "taskfilter"
    }


    object FILTER {
        const val ALL = 0
        const val NEXT = 1
        const val EXPIRED = 2
    }

}