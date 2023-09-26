package com.xht.androidnote.module.asm

import java.io.File

object Utils {
    @JvmStatic
    fun getClassFilePath(clazz: Class<*>): String? {
        // file:/Users/zhy/hongyang/repo/BlogDemo/app/build/intermediates/javac/debug/classes/
        val buildDir = clazz.protectionDomain?.codeSource?.location?.file
        val fileName = clazz.simpleName + ".class"
        val file =
            File(buildDir + clazz.getPackage().name.replace("[.]".toRegex(), "/") + "/", fileName)
        return file.absolutePath
    }
}