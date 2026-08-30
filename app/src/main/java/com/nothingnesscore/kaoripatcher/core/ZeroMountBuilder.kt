package com.nothingnesscore.kaoripatcher.core

import java.io.File

object ZeroMountBuilder {
    fun buildReplacementModule(originalPath: String, modifiedFile: File): Boolean {
        val moduleDir = "/data/adb/modules/kaorios_framework"
        val systemDir = "$moduleDir/system/framework"
        
        // Ensure module directories exist
        if (!RootShell.execute("mkdir -p $systemDir")) {
            return false
        }
        
        // Move the modified file to the module's target directory
        if (!RootShell.execute("cp ${modifiedFile.absolutePath} $systemDir/framework.jar")) {
            return false
        }
        
        // ZeroMount requires setting permissions
        RootShell.execute("chmod 644 $systemDir/framework.jar")
        RootShell.execute("chown root:root $systemDir/framework.jar")
        
        // Create module.prop
        val moduleProp = """
            id=kaorios_framework
            name=KaoriOS Framework (ZeroMount)
            version=v2.0.4.0
            versionCode=2040
            author=nothingnesscore
            description=ZeroMount compatible framework.jar module with KaoriOS payloads.
        """.trimIndent()
        
        val propFile = File(modifiedFile.parentFile, "module.prop")
        propFile.writeText(moduleProp)
        
        RootShell.execute("cp ${propFile.absolutePath} $moduleDir/module.prop")
        RootShell.execute("chmod 644 $moduleDir/module.prop")
        
        // Optional: write a script to register susfs bindings explicitly if auto-mount is disabled
        // For ZeroMount, the folder structure in /data/adb/modules is often enough if CONFIG_KSU_SUSFS_AUTO_ADD_SUS_KSU_DEFAULT_MOUNT=y
        
        return true
    }
}
