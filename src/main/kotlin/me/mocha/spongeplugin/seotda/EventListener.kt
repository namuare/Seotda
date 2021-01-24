package me.mocha.spongeplugin.seotda

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.Root
import org.spongepowered.api.event.item.inventory.InteractItemEvent

object EventListener {

    val logger = Seotda.instance.logger

    @Listener
    fun onInteract(event: InteractItemEvent, @Root player: Player) {

    }

}