package me.mocha.spongeplugin.seotda.command

import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import me.mocha.spongeplugin.seotda.util.MessageBuilder
import me.mocha.spongeplugin.seotda.util.defer
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
        .arguments(GenericArguments.optional(GenericArguments.integer(Text.of("num"))))
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
        .child(start, "start", "play")
        .child(end, "stop", "end")
        .build()

    private fun addPlayer(src: CommandSource, args: CommandContext): CommandResult = defer {
        it.defer { src.sendMessage(currentPlayers()) }

        val players = args.getAll<Player>(Text.of("player"))
        if (SeotdaGameService.addPlayer(*players.toTypedArray())) {
            src.sendMessage(MessageBuilder.success("command.addplayer"))
            CommandResult.successCount(players.size)
        } else {
            if (SeotdaGameService.isStarted) {
                src.sendMessage(MessageBuilder.error("error.whileplaying"))
            } else {
                src.sendMessage(MessageBuilder.error("error.addplayer"))
            }
            CommandResult.empty()
        }
    }

    private fun removePlayer(src: CommandSource, args: CommandContext): CommandResult = defer {
        it.defer { src.sendMessage(currentPlayers()) }

        val players = args.getAll<Player>(Text.of("player"))
        if (SeotdaGameService.removePlayer(*players.toTypedArray())) {
            src.sendMessage(MessageBuilder.success("command.removeplayer"))
            CommandResult.successCount(players.size)
        } else {
            if (SeotdaGameService.isStarted) {
                src.sendMessage(MessageBuilder.error("error.whileplaying"))
            } else {
                src.sendMessage(MessageBuilder.error("error.removeplayer"))
            }
            CommandResult.empty()
        }
    }

    private fun start(src: CommandSource, args: CommandContext): CommandResult {
        return if (SeotdaGameService.isStarted) {
            src.sendMessage(MessageBuilder.error("error.alreadystarted"))
            CommandResult.empty()
        } else {
            val num = args.getOne<Int>(Text.of("num")).orElse(1)
            SeotdaGameService.start(num)
            src.sendMessage(MessageBuilder.success("command.gamestart"))
            CommandResult.success()
        }
    }

    private fun end(src: CommandSource, args: CommandContext): CommandResult {
        SeotdaGameService.end()
        src.sendMessage(MessageBuilder.success("command.gamestop"))
        return CommandResult.success()
    }

    private fun currentPlayers(): Text {
        val playersPerLimit = "${SeotdaGameService.players.size}/${SeotdaGameService.players.limit}"
        val players = SeotdaGameService.players.joinToString { it.name }
        return MessageBuilder.success("command.currentplayers", "players_per_limit" to playersPerLimit, "players" to players)
    }


}