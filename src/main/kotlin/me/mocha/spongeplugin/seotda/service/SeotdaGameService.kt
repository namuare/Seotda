package me.mocha.spongeplugin.seotda.service

import me.mocha.spongeplugin.seotda.Seotda
import me.mocha.spongeplugin.seotda.task.WoolRouletteTask
import me.mocha.spongeplugin.seotda.util.LimitedQueue
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

object SeotdaGameService {
    private val logger = Seotda.instance.logger

    val MAX_PLAYER: Int
    val players: LimitedQueue<Player>

    var isPlaying = false
        private set

    init {
        val config = HoconConfigurationLoader.builder().setPath(Seotda.instance.configPath).build()
        val root = config.load()

        this.MAX_PLAYER = root.getNode("max_player").getInt(8)
        this.players = LimitedQueue(this.MAX_PLAYER)
    }

    fun start() {
        isPlaying = true
        logger.info("game start with player ${this.players.joinToString { it.name }}.")

        this.players.forEach {
            Task.builder().interval(500, TimeUnit.MILLISECONDS)
                .execute(WoolRouletteTask(it))
                .submit(Seotda.instance)
        }
    }

    fun end() {
        isPlaying = false
        this.players.clear()
        logger.info("seotda game has ended.")
    }

    fun addPlayer(vararg players: Player): Boolean {
        return if (!isPlaying) this.players.addAll(players) else false
    }

    fun removePlayer(vararg players: Player): Boolean {
        return if (!isPlaying) this.players.removeAll(players) else false
    }

}