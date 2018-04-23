package com.otamate.android.kotlinaac

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainViewModel = ViewModelProviders.of(this)[MainViewModel::class.java]

        mainViewModel.getProgressLiveData().observe(this, Observer<MainViewModel.ProgressData> { progressData ->

            if (progressData != null) {
                Log.d(TAG, "Progress: " + progressData.progress)
                progressData.progress = mainViewModel.getProgressData().progress
            }
        })

        mainViewModel.getViewStateLiveData().observe(this, Observer<MainViewModel.ViewStateData> { viewStateData ->

            Log.d(TAG, "View State: " + viewStateData!!)

            // UI changed due to user action, e.g. button click
            handleUIStateChange()
        })

        // UI changed due to Activity reload
        handleUIStateChange()

        fabRestart.setOnClickListener {
            mainViewModel.setViewStateData(mainViewModel.getViewStateData().copy(isStarted = false, isFinished = false))
        }

        buttonBegin.setOnClickListener {
            mainViewModel.getProgressData().progress = 0
            mainViewModel.setViewStateData(mainViewModel.getViewStateData().copy(isStarted = true, isFinished = false))
        }
    }

    private fun handleUIStateChange() {
        if (mainViewModel.getViewStateLiveData().value!!.isStarted) {
            buttonBegin.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        } else {
            buttonBegin.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }

        if (mainViewModel.getViewStateLiveData().value!!.isFinished) {
            fabRestart.visibility = View.VISIBLE
        } else {
            fabRestart.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
