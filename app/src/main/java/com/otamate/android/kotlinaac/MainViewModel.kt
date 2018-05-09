package com.otamate.android.kotlinaac

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import kotlin.concurrent.thread

class MainViewModel : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    data class ViewStateData (
        var isStarted: Boolean = false,
        var isFinished: Boolean = false
    )

    data class ProgressData (
        var progress: Int = 0
    )

    private var liveDataViewStateData: MutableLiveData<ViewStateData> = MutableLiveData()
    private val liveDataProgressData: MutableLiveData<ProgressData> = MutableLiveData()

    init {
        liveDataViewStateData.value = ViewStateData()
        liveDataProgressData.value = ProgressData()
    }

    fun getViewStateLiveData(): LiveData<ViewStateData> {
        return liveDataViewStateData
    }

    fun getProgressLiveData(): LiveData<ProgressData> {
        return liveDataProgressData
    }

    fun getViewStateData(): ViewStateData {
        return getViewStateLiveData().value!!
    }

    fun getProgressData(): ProgressData {
        return getProgressLiveData().value!!
    }


    fun setViewStateData(newViewStateData: ViewStateData) {
        if (!liveDataViewStateData.value!!.isStarted && newViewStateData.isStarted) {
            start()
        }

        liveDataViewStateData.value = newViewStateData
    }

    fun start() {
        Log.d(TAG, "Starting.")

        thread(start = true) {
            while (getProgressData().progress < 100) {
                liveDataProgressData.postValue(getProgressData().copy(progress = getProgressData().progress + 1))
                Thread.sleep(100)

                Log.d(TAG, "Progress: " + getProgressData().progress)
            }

            liveDataViewStateData.postValue(getViewStateData().copy(isFinished = true))
            Log.d(TAG, "Done.")
        }
    }
}
