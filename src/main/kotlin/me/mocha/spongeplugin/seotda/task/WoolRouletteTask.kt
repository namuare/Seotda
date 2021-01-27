package me.mocha.spongeplugin.seotda.task

import me.mocha.spongeplugin.seotda.wool.WoolRoulette
import me.mocha.spongeplugin.seotda.wool.SeotdaWool
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.entity.Hotbar
import org.spongepowered.api.item.inventory.query.QueryOperationTypes

private val wools = SeotdaWool.values()

class WoolRouletteTask(val player: Player) : Runnable {

    var current: Int = 0
        private set

    override fun run() {
        val hotbar = player.inventory.query<Hotbar>(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar::class.java))
        hotbar[SlotIndex(0)] = WoolRoulette.createItemStack(wools[current])
        current = if (current + 1 >= wools.size) 0 else current + 1
    }

}