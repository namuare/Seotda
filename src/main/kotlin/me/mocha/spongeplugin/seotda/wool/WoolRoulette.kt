package me.mocha.spongeplugin.seotda.wool

import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.entity.Hotbar
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

object WoolRoulette {

    val itemName = Text.of(
        TextColors.RED, "r",
        TextColors.GOLD, "a",
        TextColors.YELLOW, "n",
        TextColors.GREEN, "d",
        TextColors.AQUA, "o",
        TextColors.DARK_BLUE, "m",
        TextColors.DARK_PURPLE, "!"
    )

    fun createItemStack(wool: SeotdaWool) = wool.createItemStack().apply {
        offer(Keys.DISPLAY_NAME, itemName)
    }

    fun isRoulette(item: ItemStack) = item.get(Keys.DISPLAY_NAME).orElseGet { Text.of() } == itemName

    fun offerRandomWool(player: Player) {
        val hotbar = player.inventory.query<Hotbar>(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar::class.java))
        hotbar[SlotIndex(1)] = SeotdaWool.random().createItemStack()
    }
}