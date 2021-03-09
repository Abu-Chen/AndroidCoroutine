package com.abu.androidcoroutine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abu.androidcoroutine.task.Task
import com.abu.androidcoroutine.task.TaskCallback
import kotlinx.coroutines.*

class MainViewModel : ViewModel() {
    val uiText: MutableLiveData<String> = MutableLiveData()

    /** Run task */
    fun run() {
        viewModelScope.launch(Dispatchers.Default) {
            val response = Task.get().run(1)
            uiText.postValue("response:$response\nfinish")
        }
    }

    /** Run task, retry when fail */
    fun runRetry(retryTime: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            var response: String
            var exeCount = 1
            do {
                response = Task.get().run(1)
                uiText.postValue("Run count:$exeCount, response = $response")
            } while (response == Task.FAIL && exeCount++ <= retryTime)
            uiText.postValue("response:$response\nfinish")
        }
    }

    /** Run task, retry when fail or timeout */
    fun runRetryWithTimeout(retryTime: Int, timeout: Long, costTime: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            var response: String?
            var exeCount = 1
            var dynamicCostTime = costTime
            do {
                response = withTimeoutOrNull(timeout*1000) {
                    Task.get().run(dynamicCostTime--)
                }
                if(response == null) {
                    uiText.postValue("Timeout with $timeout sec.")
                }
                uiText.postValue("Run count:$exeCount, response = $response ")
            } while (
                (response == null)
                && exeCount++ <= retryTime
            )
            uiText.postValue("response:$response\nfinish")
        }
    }

    fun runMultiJobs() {
        GlobalScope.launch(Dispatchers.Default) {
            val jobs: List<Job> = (1..5).map { jobId ->
                GlobalScope.launch(Dispatchers.Default) {
                    Task.get().runLoop(jobId.toString(), jobId, 1, object : TaskCallback{
                        override fun onUpdate(state: String) {
                            uiText.postValue("$state")
                        }
                    })
                }
            }
            jobs.joinAll()
            uiText.postValue("Finish")
        }
        uiText.postValue("Start")
    }
}
