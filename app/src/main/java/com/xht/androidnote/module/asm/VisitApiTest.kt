package com.xht.androidnote.module.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.FileInputStream


class VisitApiTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val clazz = ASMTestActivity::class.java
            val clazzFilePath = Utils.getClassFilePath(clazz)
            val classReader = ClassReader(FileInputStream(clazzFilePath))
            val classWriter = ClassWriter(ClassWriter.COMPUTE_MAXS)
            val classVisitor = MyClassVisitor(classWriter)
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        }
    }

}