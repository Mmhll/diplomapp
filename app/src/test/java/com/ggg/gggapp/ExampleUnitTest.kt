package com.ggg.gggapp

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
data class Smth(
    val id1: Int,
    val id2: Int,
)

class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val list = arrayListOf(Smth(5,1), Smth(3,1), Smth(3,2))
        println(list.sortedWith(compareByDescending<Smth> { smth -> smth.id1 }.thenByDescending { smth-> smth.id2 }))
    }
}