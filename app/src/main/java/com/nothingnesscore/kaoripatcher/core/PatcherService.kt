package com.nothingnesscore.kaoripatcher.core

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object PatcherService {

    suspend fun patchFramework(context: Context, cacheDir: File): String = withContext(Dispatchers.IO) {
        if (!RootShell.hasRootAccess()) {
            return@withContext "Error: No root access granted."
        }

        val originalFile = "/system/framework/framework.jar"
        val workDir = File(cacheDir, "patcher_workspace")
        workDir.mkdirs()
        
        val localCopy = File(workDir, "framework.jar")
        val decompileDir = File(workDir, "framework_decompiled")

        // 1. Copy framework.jar from system
        val copySuccess = RootShell.execute("cp $originalFile ${localCopy.absolutePath}")
        if (!copySuccess) {
            return@withContext "Error: Failed to copy framework.jar from system."
        }

        // 2. Unpack baksmali/smali and patch assets to execute on device
        val baksmaliJar = extractAsset(context, "baksmali.jar", workDir)
        val smaliJar = extractAsset(context, "smali.jar", workDir)
        val kaoriosDex = extractAsset(context, "kaorios_classes.dex", workDir)
        val patchScript = extractAsset(context, "patcher_script.sh", workDir)

        if (baksmaliJar == null || smaliJar == null || patchScript == null || kaoriosDex == null) {
            return@withContext mockPatchingPipeline(originalFile, localCopy)
        }

        // 3. Execute the actual KaoriOS shell script pipeline on device
        RootShell.execute("chmod +x ${patchScript.absolutePath}")
        
        val command = "sh ${patchScript.absolutePath} ${localCopy.absolutePath} ${decompileDir.absolutePath} ${baksmaliJar.absolutePath} ${smaliJar.absolutePath} ${kaoriosDex.absolutePath}"
        val patchSuccess = RootShell.execute(command)

        if (!patchSuccess) {
            return@withContext "Error: Dalvik VM patching failed. See logcat for details."
        }

        // 4. Build ZeroMount Module
        val moduleSuccess = ZeroMountBuilder.buildReplacementModule(originalFile, localCopy)
        if (!moduleSuccess) {
            return@withContext "Error: Failed to build ZeroMount module."
        }

        return@withContext "Success! KaoriOS ZeroMount module created at /data/adb/modules/kaorios_framework. Please reboot."
    }

    private fun mockPatchingPipeline(originalPath: String, localCopy: File): String {
        val moduleSuccess = ZeroMountBuilder.buildReplacementModule(originalPath, localCopy)
        if (!moduleSuccess) return "Error: Failed to build ZeroMount module."
        return "Success (Simulated)! Assets missing. ZeroMount module created without patches. Please reboot."
    }

    private fun extractAsset(context: Context, assetName: String, destDir: File): File? {
        return try {
            val destFile = File(destDir, assetName)
            if (destFile.exists()) return destFile
            
            context.assets.open(assetName).use { inputStream ->
                FileOutputStream(destFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            destFile
        } catch (e: Exception) {
            null
        }
    }
}
