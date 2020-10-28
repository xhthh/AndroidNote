package com.xht.androidnote.module.kotlin

class Test {
    init {
        val a: Int
        a = 1
        println(a)


        var list: List<Country>? = null

        val instance = Test()
        val filterCountries = filterCountries(list!!, instance::isBigEuropeanCountry)

        filterCountries(list!!, { country ->
            country.continent == "EU" && country.population > 10000
        })
    }


    fun filterCountries(countries: List<Country>, test: (Country) -> Boolean): List<Country> {
        val res = mutableListOf<Country>()
        for (c in countries) {
            if (test(c)) {
                res.add(c)
            }
        }
        return res
    }

    fun isBigEuropeanCountry(country: Country): Boolean {
        return country.continent == "EU" && country.population > 10000
    }
}

fun main() {
    val foo = foo(2)
    println("foo = $foo")


}

fun foo(x: Int): Int {
    fun double(y: Int): Int {
        return y * 2
    }
    return double(x)
}

data class Country(val name: String,
                   val continent: String,
                   val population: Int)

