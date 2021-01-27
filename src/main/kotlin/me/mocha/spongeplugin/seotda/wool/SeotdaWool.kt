package me.mocha.spongeplugin.seotda.wool

import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.text.Text
import java.util.*

enum class SeotdaWool(val score: Int, val woolCode: Int) {
    WHITE(0, 0),
    RED(1, 14),
    ORANGE(2, 1),
    YELLOW(3, 4),
    GREEN(4, 5),
    SKY(5, 3),
    BLUE(6, 11),
    PURPLE(7, 10),
    BLACK(8, 15);

    fun createItemStack(): ItemStack = ItemStack.of(ItemTypes.WOOL).apply {
        offer(Keys.DISPLAY_NAME, Text.of(score))
        setRawData(toContainer().apply {
            set(DataQuery.of("UnsafeDamage"), woolCode)
        })
    }

    companion object {
        fun random(): SeotdaWool {
            val values = values()
            val random = Random()
            val num = random.nextInt(values.size)
            return values[num]
        }
    }
}