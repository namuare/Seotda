package me.mocha.spongeplugin.seotda.task

import me.mocha.spongeplugin.seotda.wool.SeotdaWool
import me.mocha.spongeplugin.seotda.wool.WoolRoulette
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.entity.Hotbar
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.scheduler.Task
import java.util.function.Consumer

private val wools = SeotdaWool.values()

class WoolRouletteTask(var quantity: Int, val player: Player) : Consumer<Task> {

    var current: Int = 0
        private set

    override fun accept(t: Task) {
        val hotbar = player.inventory.query<Hotbar>(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar::class.java))
        if (quantity <= 0) {
            hotbar.poll(SlotIndex(0))
            t.cancel()
            return
        }

        hotbar[SlotIndex(0)] = WoolRoulette.createItemStack(wools[current], quantity)
        current = if (current + 1 >= wools.size) 0 else current + 1
    }

    fun reduceQuantity() {
        quantity--
    }

}