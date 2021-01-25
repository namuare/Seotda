package me.mocha.spongeplugin.seotda

import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import me.mocha.spongeplugin.seotda.task.randomWoolName
import me.mocha.spongeplugin.seotda.util.SeotdaWool
import org.spongepowered.api.data.DataQuery
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.DropItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack

object EventListener {

    val logger = Seotda.instance.logger

    @Listener
    fun onInteract(event: InteractItemEvent, @Root player: Player) {
        val itemName = event.itemStack.get(Keys.DISPLAY_NAME).get()
        if (SeotdaGameService.isPlaying && itemName == randomWoolName) {
            offerRandomWool(player)
        }
    }

    @Listener
    fun onPlace(event: ChangeBlockEvent.Place, @Root player: Player) {
        val usedItem = event.cause.context.get(EventContextKeys.USED_ITEM).get()
        val itemName = usedItem.get(Keys.DISPLAY_NAME).get()
        if (SeotdaGameService.isPlaying && itemName == randomWoolName) {
            offerRandomWool(player)
            event.isCancelled = true
        }
    }

    @Listener
    fun onDrop(event: DropItemEvent) {

    }

    private fun offerRandomWool(player: Player) {
        val random = SeotdaWool.random()
        val wool = ItemStack.of(ItemTypes.WOOL).apply {
            val container = this.toContainer()
            container.set(DataQuery.of("UnsafeDamage"), random.woolCode)
            this.setRawData(container)
        }

        player.inventory.offer(wool)
    }

}