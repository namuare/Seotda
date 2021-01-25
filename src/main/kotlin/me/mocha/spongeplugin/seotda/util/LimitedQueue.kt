package me.mocha.spongeplugin.seotda.util

class LimitedQueue<T>(val limit: Int) : HashSet<T>() {

    override fun add(element: T): Boolean {
        if (size >= limit) return false
        return super.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (size + elements.size > limit) return false
        return super.addAll(elements)
    }

}