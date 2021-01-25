package me.mocha.spongeplugin.seotda.task

import me.mocha.spongeplugin.seotda.util.SeotdaWool
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.entity.Hotbar
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

private val wools = SeotdaWool.values()
val randomWoolName = Text.of(
    TextColors.RED, "r",
    TextColors.GOLD, "a",
    TextColors.YELLOW, "n",
    TextColors.GREEN, "d",
    TextColors.AQUA, "o",
    TextColors.DARK_BLUE, "m",
    TextColors.DARK_PURPLE, "!"
)

class WoolRouletteTask(val player: Player) : Runnable {

    var current: Int = 0
        private set

    override fun run() {
        val inventory = player.inventory
        val hotbar = inventory.query<Hotbar>(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar::class.java))
        hotbar[SlotIndex(0)] = ItemStack.of(ItemTypes.WOOL).apply {
            this.offer(Keys.DISPLAY_NAME, randomWoolName)
            val container = this.toContainer()
            container.set(DataQuery.of("UnsafeDamage"), wools[current].woolCode)
            this.setRawData(container)
        }
        current = if (current + 1 >= wools.size) 0 else current + 1
    }

}