package me.mocha.spongeplugin.seotda.command

import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import me.mocha.spongeplugin.seotda.util.TextBuilder
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text

object SeotdaCommand {
    private val add: CommandSpec = CommandSpec.builder()
        .permission("seotda.command.seotda.addplayer")
        .arguments(GenericArguments.allOf(GenericArguments.player(Text.of("player"))))
        .executor { src, args -> addPlayer(src, args) }
        .build()

    private val remove: CommandSpec = CommandSpec.builder()
        .permission("")
        .arguments(GenericArguments.allOf(GenericArguments.player(Text.of("player"))))
        .executor { src, args -> removePlayer(src, args) }
        .build()

    private val start: CommandSpec = CommandSpec.builder()
        .permission("")
        .executor { src, args -> start(src, args) }
        .build()

    private val end: CommandSpec = CommandSpec.builder()
        .permission("")
        .executor { src, args -> end(src, args) }
        .build()

    val spec: CommandSpec = CommandSpec.builder()
        .permission("seotda.command.seotda")
        .child(add, "add")
        .child(remove, "remove")
        .child(start, "start")
        .child(end, "end")
        .build()

    private fun addPlayer(src: CommandSource, args: CommandContext): CommandResult {
        val players = args.getAll<Player>(Text.of("player"))
        return if (SeotdaGameService.addPlayer(*players.toTypedArray())) {
            src.sendMessage(TextBuilder.success("Successfully added players."))
            CommandResult.successCount(players.size)
        } else {
            if (SeotdaGameService.isPlaying) {
                src.sendMessage(TextBuilder.error("Cannot add or remove players while playing."))
            } else {
                src.sendMessage(TextBuilder.error("Could not add players in game queue. It may have already been added to the maximum."))
            }
            CommandResult.empty()

        }
    }

    private fun removePlayer(src: CommandSource, args: CommandContext): CommandResult {
        val players = args.getAll<Player>(Text.of("player"))
        return if (SeotdaGameService.removePlayer(*players.toTypedArray())) {
            src.sendMessage(TextBuilder.success("Successfully removed players."))
            CommandResult.successCount(players.size)
        } else {
            if (SeotdaGameService.isPlaying) {
                src.sendMessage(TextBuilder.error("Cannot add or remove players while playing."))
            } else {
                src.sendMessage(TextBuilder.error("Could not remove players in game queue. Please check and try again."))
            }
            CommandResult.empty()
        }
    }

    private fun start(src: CommandSource, args: CommandContext): CommandResult {
        return if (SeotdaGameService.isPlaying) {
            src.sendMessage(TextBuilder.error("Seotda game has already started."))
            CommandResult.empty()
        } else {
            SeotdaGameService.start()
            src.sendMessage(TextBuilder.success("Seotda game begins!"))
            CommandResult.success()
        }
    }

    private fun end(src: CommandSource, args: CommandContext): CommandResult {
        SeotdaGameService.end()
        src.sendMessage(TextBuilder.success("Seotda game has ended. resetting game..."))
        return CommandResult.success()
    }


}