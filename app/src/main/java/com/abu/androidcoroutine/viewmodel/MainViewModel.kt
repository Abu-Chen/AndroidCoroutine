package com.abu.androidcoroutine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abu.androidcoroutine.task.Task
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    val state: MutableLiveData<String> = MutableLiveData()

    /** Run task */
    fun run() {
        GlobalScope.launch(Dispatchers.Default) {
            val response = Task.get().costSec(1)
            state.postValue("response:$response\nfinish")
        }
    }

    /** Run task, retry when fail */
    fun runRetry(retryTime: Int) {
        GlobalScope.launch(Dispatchers.Default) {
            var response: String
            var exeCount = 1
            do {
                response = Task.get().costSec(1)
                state.postValue("Run count:$exeCount, response = $response")
            } while (response == Task.FAIL && exeCount++ <= retryTime)
            state.postValue("response:$response\nfinish")
        }
    }

    /** Run task, retry when fail or timeout */
    fun runRetryWithTimeout(retryTime: Int, timeout: Long, costTime: Int) {
        GlobalScope.launch(Dispatchers.Default) {
            var response: String?
            var exeCount = 1
            var dynamicCostTime = costTime
            do {
                response = withTimeoutOrNull(timeout*1000) {
                    Task.get().costSec(dynamicCostTime--)
                }
                if(response == null) {
                    state.postValue("Timeout with $timeout sec.")
                }
                state.postValue("Run count:$exeCount, response = $response ")
            } while (
                (response == null)
                && exeCount++ <= retryTime
            )
            state.postValue("response:$response\nfinish")
        }
    }
}
