package com.ggg.gggapp

import android.content.res.Resources.NotFoundException
import android.util.Log
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

    @Test
    fun isCorrect() {
        val oldList = arrayListOf(1,2,3,4)
        val newList = arrayListOf(2,3)
        val notFoundList = arrayListOf<Int>()
        oldList.forEach{
            if (!newList.contains(it)){
                notFoundList.add(it)
            }
        }
        assert(notFoundList.size == 2)
    }
}