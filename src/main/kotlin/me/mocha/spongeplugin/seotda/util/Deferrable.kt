package me.mocha.spongeplugin.seotda.util

class Deferrable {
    val actions = mutableListOf<() -> Unit>()

    fun defer(f: () -> Unit) {
        actions.add(f)
    }

    fun execute() {
        actions.forEach { it() }
    }
}

fun <T> defer(f: (Deferrable) -> T): T {
    val deferrable = Deferrable()
    try {
        return f(deferrable)
    } finally {
        deferrable.execute()
    }
}