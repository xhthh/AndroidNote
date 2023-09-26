package com.xht.androidnote.module.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MyClassVisitor(private val classWriter: ClassWriter) :
    ClassVisitor(Opcodes.ASM5, classWriter) {

    override fun visitMethod(
        access: Int,
        name: String,
        descriptor: String?,
        signature: String?,
        exceptions: Array<String?>?,
    ): MethodVisitor? {
        var mv = super.visitMethod(access, name, descriptor, signature, exceptions)
        if ("test" == name) {
            mv = object : MethodVisitor(Opcodes.ASM5, mv) {
                override fun visitCode() {
                    super.visitCode()
                    mv.visitFieldInsn(
                        Opcodes.GETSTATIC,
                        "java/lang/System",
                        "out",
                        "Ljava/io/PrintStream;"
                    )
                    mv.visitLdcInsn("Entering method test")
                    mv.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "java/io/PrintStream",
                        "println",
                        "(Ljava/lang/String;)V",
                        false
                    )
                }

                override fun visitInsn(opcode: Int) {
                    if (opcode == Opcodes.RETURN) {
                        mv.visitFieldInsn(
                            Opcodes.GETSTATIC,
                            "java/lang/System",
                            "out",
                            "Ljava/io/PrintStream;"
                        )
                        mv.visitLdcInsn("Exiting method test")
                        mv.visitMethodInsn(
                            Opcodes.INVOKEVIRTUAL,
                            "java/io/PrintStream",
                            "println",
                            "(Ljava/lang/String;)V",
                            false
                        )
                    }
                    super.visitInsn(opcode)
                }
            }
        }
        return mv
    }

}