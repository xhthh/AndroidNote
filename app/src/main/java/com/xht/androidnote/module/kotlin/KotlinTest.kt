package com.xht.androidnote.module.kotlin

class KotlinTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            var test = KotlinTest
            val amount: String? = null
            amount?.let {
                println("let------------1")
            } ?: println("let------------2")
        }
    }


}