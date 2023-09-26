package com.xht.androidnote.module.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode
import java.io.FileInputStream


class TreeApiTest {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val clazz = User::class.java
            val clazzFilePath = Utils.getClassFilePath(clazz)
            val classReader = ClassReader(FileInputStream(clazzFilePath))
            val classNode = ClassNode(Opcodes.ASM5)
            classReader.accept(classNode, 0)

            val methods = classNode.methods
            val fields = classNode.fields
            println("methods：")
            for (methodNode in methods) {
                println(methodNode.name + "，" + methodNode.desc)
            }
            println("fields：")
            for (fileNode in fields) {
                println(fileNode.name + "，" + fileNode.desc)
            }

            for (fieldNode in classNode.fields) {
                // 1
                if (fieldNode.name == "password") {
                    // 2
                    fieldNode.access = Opcodes.ACC_PUBLIC
                }
            }

            val fieldNode =
                FieldNode(Opcodes.ACC_PUBLIC or Opcodes.ACC_STATIC, "JsonChao", "I", null, null)
            classNode.fields.add(fieldNode)

            val methodNode = methods[0]

            //获取操作码的位置
            for (ainNode in methodNode.instructions.toArray()) {
                if (ainNode.opcode == Opcodes.SIPUSH && (ainNode as IntInsnNode).operand == 16) {
                }
            }

            //替换指定位置操作码
            for (ainNode in methodNode.instructions) {
                if (ainNode.opcode == Opcodes.BIPUSH && (ainNode as IntInsnNode).operand == 16) {
                    methodNode.instructions.set(ainNode, VarInsnNode(Opcodes.ALOAD, 1))
                }
            }

            //删除指定的操作码
            methodNode.instructions.remove(methodNode.instructions.get(0))


            //插入指定的操作码
            for (ainNode in methodNode.instructions) {
                if (ainNode.opcode == Opcodes.BIPUSH && (ainNode as IntInsnNode).operand == 16) {
                    methodNode.instructions.insert(
                        ainNode, MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            "java/awt/image/BufferedImage",
                            "getWidth",
                            "(Ljava/awt/image/ImageObserver;)I"
                        )
                    )

                    methodNode.instructions.insert(ainNode, InsnNode(Opcodes.ACONST_NULL))
                }
            }
        }

    }
}