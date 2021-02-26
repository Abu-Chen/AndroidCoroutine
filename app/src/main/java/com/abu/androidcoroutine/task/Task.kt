package com.abu.androidcoroutine.task

import kotlinx.coroutines.delay

class Task {
    companion object{
        private var instance: Task? = null

        fun get(): Task {
            if (instance == null) {
                instance = Task()
            }
            return instance!!
        }

        const val FAIL = "fail"
    }

    suspend fun costSec(sec: Int): String {
        delay(sec.toLong() * 1000)
        return arrayOf("Task cost $sec seconds.", FAIL).random()
    }
}