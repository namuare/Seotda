package me.mocha.spongeplugin.seotda.wool

import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.ItemStack
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

    fun createItemStack(wool: SeotdaWool, quantity: Int = 1) = wool.createItemStack(quantity).apply {
        offer(Keys.DISPLAY_NAME, itemName)
    }

    fun isRoulette(item: ItemStack) = item.get(Keys.DISPLAY_NAME).orElseGet { Text.of() } == itemName

    fun offerRandomWool(player: Player) {
        SeotdaGameService.roulettes[player]?.reduceQuantity()
        player.inventory.offer(SeotdaWool.random().createItemStack())
    }
}