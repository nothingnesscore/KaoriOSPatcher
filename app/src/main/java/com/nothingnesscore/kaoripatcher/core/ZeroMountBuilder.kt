package com.nothingnesscore.kaoripatcher.core

import java.io.File

object ZeroMountBuilder {
    fun buildModuleFromZip(zipPath: String, outputDir: String = "/data/adb/modules/kaorios_toolbox"): Boolean {
        RootShell.execute("mkdir -p $outputDir")
        RootShell.execute("unzip -o '$zipPath' -d '$outputDir'")

        // Dalvik Cache Whiteouts for ZeroMount
        val frameworkDir = "$outputDir/system/framework"
        RootShell.execute("mkdir -p $frameworkDir")
        
        val archs = listOf("arm", "arm64", "oat")
        for (arch in archs) {
            RootShell.execute("touch $frameworkDir/.wh.$arch")
        }

        RootShell.execute("touch $frameworkDir/.wh.boot-framework.oat")
        RootShell.execute("touch $frameworkDir/.wh.boot-framework.art")
        RootShell.execute("touch $frameworkDir/.wh.boot-framework.vdex")
        RootShell.execute("touch $frameworkDir/.wh.services.odex")
        RootShell.execute("touch $frameworkDir/.wh.services.vdex")
        RootShell.execute("touch $frameworkDir/.wh.services.art")

        RootShell.execute("chown -R root:root $outputDir")
        RootShell.execute("chmod -R 755 $outputDir/META-INF")
        RootShell.execute("chmod 644 $outputDir/system.prop")
        RootShell.execute("chmod 755 $outputDir/action.sh")
        RootShell.execute("chmod 755 $outputDir/service.sh")

        RootShell.execute("sed -i 's/^name=.*/name=KaoriOS Toolbox (ZeroMount Native)/' $outputDir/module.prop")
        
        return true
    }

    fun buildReplacementModule(originalPath: String, modifiedFile: File): Boolean {
        val moduleDir = "/data/adb/modules/kaorios_framework"
        val systemDir = "${moduleDir}/system/framework"
        
        if (!RootShell.execute("mkdir -p ${systemDir}")) return false
        if (!RootShell.execute("cp ${modifiedFile.absolutePath} ${systemDir}/framework.jar")) return false
        
        RootShell.execute("chmod 644 ${systemDir}/framework.jar")
        RootShell.execute("chown root:root ${systemDir}/framework.jar")
        
        val archs = listOf("arm", "arm64", "oat")
        for (arch in archs) {
            RootShell.execute("touch ${systemDir}/.wh.${arch}")
        }
        RootShell.execute("touch ${systemDir}/.wh.boot-framework.oat")
        RootShell.execute("touch ${systemDir}/.wh.boot-framework.art")
        RootShell.execute("touch ${systemDir}/.wh.boot-framework.vdex")
        
        val moduleProp = "id=kaorios_framework\nname=KaoriOS Framework (ZeroMount)\nversion=v2.0.4.0\nversionCode=2040\nauthor=nothingnesscore\ndescription=ZeroMount compatible framework.jar module with KaoriOS payloads."
        val propFile = File(modifiedFile.parentFile, "module.prop")
        propFile.writeText(moduleProp)
        
        RootShell.execute("cp ${propFile.absolutePath} ${moduleDir}/module.prop")
        RootShell.execute("chmod 644 ${moduleDir}/module.prop")
        
        return true
    }
}
