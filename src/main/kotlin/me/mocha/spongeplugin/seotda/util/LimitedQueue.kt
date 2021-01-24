package me.mocha.spongeplugin.seotda.util

import java.util.*

class LimitedQueue<T>(val limit: Int) : LinkedList<T>() {

    override fun add(element: T): Boolean {
        if (size >= limit) return false
        return super.add(element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (size + elements.size >= limit) return false
        return super.addAll(index, elements)
    }

}