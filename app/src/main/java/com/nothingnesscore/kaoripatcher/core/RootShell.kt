package com.nothingnesscore.kaoripatcher.core

import java.io.BufferedReader
import java.io.InputStreamReader

object RootShell {
    fun execute(command: String): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            val exitCode = process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val output = reader.readText()
            val errorReader = BufferedReader(InputStreamReader(process.errorStream))
            val error = errorReader.readText()
            
            if (exitCode != 0) {
                println("Root shell error: $error")
            }
            exitCode == 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun executeWithOutput(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            reader.readText().trim()
        } catch (e: Exception) {
            ""
        }
    }
    
    fun hasRootAccess(): Boolean {
        return execute("id")
    }
    
    fun isZeroMountInstalled(): Boolean {
        // ZeroMount usually installs here
        val ksu = execute("test -d /data/adb/ksu/modules/zeromount")
        val magisk = execute("test -d /data/adb/modules/zeromount")
        return ksu || magisk
    }
}
