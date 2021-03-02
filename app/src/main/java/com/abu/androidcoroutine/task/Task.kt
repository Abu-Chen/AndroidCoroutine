package com.abu.androidcoroutine.task

import kotlinx.coroutines.delay

class Task {
    companion object {
        private var instance: Task? = null

        fun get(): Task {
            if (instance == null) {
                instance = Task()
            }
            return instance!!
        }

        const val FAIL = "fail"
    }

    private suspend fun costSec(sec: Int) {
        delay(sec.toLong() * 1000)
    }

    suspend fun run(costSec: Int): String {
        costSec(costSec)
        return arrayOf("Task cost $costSec seconds.", FAIL).random()
    }

    suspend fun runLoop(jobId: String, loopCount: Int, costSec: Int, listener: TaskCallback) {
        for (i in 0 until loopCount) {
            delay(costSec + jobId.toLong() * 100L)
            listener.onUpdate("job id:$jobId, loop index:$i")
        }
    }
}

interface TaskCallback {
    fun onUpdate(state: String)
}