package me.mocha.spongeplugin.test

import me.mocha.spongeplugin.seotda.util.LimitedQueue
import org.junit.Test

class LimitedQueueTest {

    @Test
    fun addTest() {
        val limit = 3
        val queue = LimitedQueue<String>(limit)
        queue.addAll(listOf("1", "2"))

        assert(!queue.add("1")) // False: When trying to add items that already contains
        assert(queue.add("3")) // True: When trying to add an item that is not contained, without exceeding the limit
        assert(!queue.add("4")) // False: When trying to add more than the maximum.
    }

    @Test
    fun addAllTest() {
        val limit = 5
        val queue = LimitedQueue<String>(limit)
        queue.add("1")

        assert(!queue.addAll(listOf("1"))) // False: When trying to add items that already contains
        assert(queue.addAll(listOf("1", "2", "3")))
        assert(!queue.addAll(listOf("4")))
        println(queue.joinToString())
    }
}