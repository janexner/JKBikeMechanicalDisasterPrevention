package com.exner.tools.jkbikemechanicaldisasterprevention.database.tools

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class ArchiveDataWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val cleanupSuccessful: Boolean
        try {
            cleanupSuccessful = archiveOldActivities()
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure()
        }

        // Indicate whether the task done successfully
        return if (cleanupSuccessful) Result.success() else Result.retry()
    }

    private fun archiveOldActivities(): Boolean {
        return true
    }
}
