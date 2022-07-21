package com.ola.recoverunsold

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ola.recoverunsold.services.BackgroundFcmTokenSender
import com.ola.recoverunsold.ui.navigation.NavigationManager
import com.ola.recoverunsold.ui.theme.RecoverUnsoldTheme

class MainActivity : ComponentActivity() {
    private val jobId = 1001
    private val refreshInterval: Long = 15 * 60 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecoverUnsoldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val snackbarHostState = remember { SnackbarHostState() }
                    NavigationManager(
                        navHostController = rememberNavController(),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
        scheduleJob()
    }

    private fun scheduleJob() {
        val jobScheduler = applicationContext
            .getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val componentName = ComponentName(this, BackgroundFcmTokenSender::class.java)
        val jobInfo = JobInfo
            .Builder(jobId, componentName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setPeriodic(refreshInterval)
            .build()
        jobScheduler.schedule(jobInfo)
    }
}