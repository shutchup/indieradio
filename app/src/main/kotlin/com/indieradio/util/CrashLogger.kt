package com.indieradio.util

import android.content.Context
import android.util.Log
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Simple crash logger that writes crashes to internal storage
 * Can be viewed through Debug screen in the app
 */
class CrashLogger(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    init {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        try {
            logCrash(throwable)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to log crash", e)
        } finally {
            // Call default handler to show system crash dialog
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun logCrash(throwable: Throwable) {
        val crashFile = File(context.filesDir, CRASH_LOG_FILE)
        val timestamp = dateFormat.format(Date())

        val stackTrace = StringWriter().apply {
            throwable.printStackTrace(PrintWriter(this))
        }.toString()

        val crashReport = buildString {
            appendLine("=" .repeat(60))
            appendLine("CRASH REPORT - $timestamp")
            appendLine("=" .repeat(60))
            appendLine()
            appendLine("Exception: ${throwable.javaClass.simpleName}")
            appendLine("Message: ${throwable.message ?: "No message"}")
            appendLine()
            appendLine("Stack Trace:")
            appendLine(stackTrace)
            appendLine()
            appendLine("=" .repeat(60))
            appendLine()
        }

        // Append to crash log (keep last 10 crashes)
        val existingLog = if (crashFile.exists()) {
            crashFile.readText()
        } else {
            ""
        }

        val newLog = crashReport + existingLog

        // Keep only last ~50KB of logs
        val trimmedLog = if (newLog.length > 50000) {
            newLog.substring(0, 50000)
        } else {
            newLog
        }

        crashFile.writeText(trimmedLog)
        Log.e(TAG, "Crash logged to: ${crashFile.absolutePath}")
    }

    companion object {
        private const val TAG = "CrashLogger"
        private const val CRASH_LOG_FILE = "crash_log.txt"

        /**
         * Get crash log content
         */
        fun getCrashLog(context: Context): String {
            val crashFile = File(context.filesDir, CRASH_LOG_FILE)
            return if (crashFile.exists()) {
                crashFile.readText()
            } else {
                "No crashes logged"
            }
        }

        /**
         * Clear crash log
         */
        fun clearCrashLog(context: Context) {
            val crashFile = File(context.filesDir, CRASH_LOG_FILE)
            if (crashFile.exists()) {
                crashFile.delete()
            }
        }
    }
}
