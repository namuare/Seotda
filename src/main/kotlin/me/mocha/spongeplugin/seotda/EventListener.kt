package me.mocha.spongeplugin.seotda

import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import me.mocha.spongeplugin.seotda.wool.SeotdaWool
import me.mocha.spongeplugin.seotda.wool.WoolRoulette
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.block.ChangeBlockEvent
import org.spongepowered.api.event.cause.EventContextKeys
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.event.item.inventory.DropItemEvent
import org.spongepowered.api.event.item.inventory.InteractItemEvent

object EventListener {

    val logger = Seotda.instance.logger

    @Listener
    fun onInteract(event: InteractItemEvent, @Root player: Player) {
        val item = event.itemStack.createStack()

        if (SeotdaGameService.isPlaying(player) && WoolRoulette.isRoulette(item)) {
            offerRandomWool(player)
        }
    }

    @Listener
    fun onPlace(event: ChangeBlockEvent.Place, @Root player: Player) {
        val snapshot = event.cause.context.get(EventContextKeys.USED_ITEM).get()
        val item = snapshot.createStack()

        if (SeotdaGameService.isPlaying(player) && WoolRoulette.isRoulette(item)) {
            offerRandomWool(player)
            event.isCancelled = true
        }
    }

    @Listener
    fun onDrop(event: DropItemEvent.Pre) {
        event.context.get(EventContextKeys.PLAYER).orElse(null)?.let { player ->
            if (SeotdaGameService.isPlaying(player)) {
                event.droppedItems.forEach {
                    if (WoolRoulette.isRoulette(it.createStack())) {
                        event.isCancelled = true
                    }
                }
            }
        }
    }

    @Listener
    fun onInventoryClick(event: ClickInventoryEvent) {
        event.context.get(EventContextKeys.OWNER).orElse(null)?.let { player ->
            if (player is Player && SeotdaGameService.isPlaying(player)) {
                event.transactions.forEach { t ->
                    if (WoolRoulette.isRoulette(t.original.createStack())) event.isCancelled = true
                    if (WoolRoulette.isRoulette(t.final.createStack())) event.isCancelled = true
                    if (WoolRoulette.isRoulette(t.default.createStack())) event.isCancelled = true
                }
            }
        }
    }

    fun offerRandomWool(player: Player) {
        SeotdaGameService.roulettes[player]?.reduceQuantity()
        player.inventory.offer(SeotdaWool.random().createItemStack())
    }

}