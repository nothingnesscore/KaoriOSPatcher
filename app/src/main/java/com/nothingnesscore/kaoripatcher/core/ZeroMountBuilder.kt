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
}
