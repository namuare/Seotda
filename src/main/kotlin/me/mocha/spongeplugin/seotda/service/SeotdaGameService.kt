package me.mocha.spongeplugin.seotda.service

import me.mocha.spongeplugin.seotda.Seotda
import me.mocha.spongeplugin.seotda.task.WoolRouletteTask
import me.mocha.spongeplugin.seotda.util.LimitedQueue
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

object SeotdaGameService {
    private val logger = Seotda.instance.logger

    val MAX_PLAYER: Int
    val players: LimitedQueue<Player>
    val tasks = mutableListOf<Task>()
    val roulettes = mutableMapOf<Player, WoolRouletteTask>()

    var isStarted = false
        private set

    init {
        val config = YAMLConfigurationLoader.builder().setPath(Seotda.instance.configPath.resolve("config.yml")).build()
        val root = config.load()

        this.MAX_PLAYER = root.getNode("max_player").getInt(8)
        this.players = LimitedQueue(this.MAX_PLAYER)
    }

    fun start(num: Int = 2) {
        isStarted = true
        logger.info("game start with player ${this.players.joinToString { it.name }}.")

        this.players.forEach {
            val task = WoolRouletteTask(num * 2, it)
            roulettes[it] = task
            tasks.add(Task.builder().interval(100, TimeUnit.MILLISECONDS)
                .execute(task)
                .submit(Seotda.instance))
        }
    }

    fun end() {
        isStarted = false
        logger.info("seotda game has ended.")

        tasks.forEach { task -> task.cancel() }

        this.players.clear()
        this.tasks.clear()
    }

    fun addPlayer(vararg players: Player): Boolean {
        return if (!isStarted) this.players.addAll(players) else false
    }

    fun removePlayer(vararg players: Player): Boolean {
        return if (!isStarted) this.players.removeAll(players) else false
    }

    fun isInQueue(player: Player) = this.players.contains(player)

    fun isPlaying(player: Player) = isStarted && isInQueue(player)

}