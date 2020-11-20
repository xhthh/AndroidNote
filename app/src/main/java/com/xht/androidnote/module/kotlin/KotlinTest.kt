package com.xht.androidnote.module.kotlin

class KotlinTest {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val test = KotlinTest()
//            test.test1()
//            test.test2()
            test.test3()
        }


    }

    private fun test3() {
        val a = object : AA {
            override fun a() {
                println("a invoked")
            }
        }

        a.a()

        val aa = object {
            fun a() {
                println("a invoked 不实现任何类")
            }
        }
        aa.a()

        val aaa = object : AA, BB {
            var count = 1

            fun test() {
                count++
                println("count=$count")
            }

            override fun a() {
                println("a invoked")
            }

            override fun b() {
                println("b invoked")
            }

        }

        aaa.a()
        aaa.b()
        aaa.test()
    }


    /**
     * 测试伴生对象--->静态内部类
     */
    private fun test2() {
        CTest.method()
        println(CTest.count)

        //单例类
        Singleton.test()
    }


    /**
     * 三元运算 if else 测试
     */
    private fun test1() {
        val amount: String? = null
        amount?.let {
            println("let------------1")
        } ?: println("let------------2")
    }


    interface AA {
        fun a()
    }

    interface BB {
        fun b()
    }

}