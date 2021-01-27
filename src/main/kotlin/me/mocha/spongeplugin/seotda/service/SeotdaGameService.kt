package me.mocha.spongeplugin.seotda.service

import me.mocha.spongeplugin.seotda.Seotda
import me.mocha.spongeplugin.seotda.task.WoolRouletteTask
import me.mocha.spongeplugin.seotda.util.LimitedQueue
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.entity.Hotbar
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

object SeotdaGameService {
    private val logger = Seotda.instance.logger

    val MAX_PLAYER: Int
    val players: LimitedQueue<Player>
    val tasks = mutableListOf<Task>()

    var isStarted = false
        private set

    init {
        val config = YAMLConfigurationLoader.builder().setPath(Seotda.instance.configPath.resolve("config.yml")).build()
        val root = config.load()

        this.MAX_PLAYER = root.getNode("max_player").getInt(8)
        this.players = LimitedQueue(this.MAX_PLAYER)
    }

    fun start() {
        isStarted = true
        logger.info("game start with player ${this.players.joinToString { it.name }}.")

        this.players.forEach {
            tasks.add(Task.builder().interval(500, TimeUnit.MILLISECONDS)
                .execute(WoolRouletteTask(it))
                .submit(Seotda.instance))
        }
    }

    fun end() {
        isStarted = false
        logger.info("seotda game has ended.")

        tasks.forEach { it.cancel() }
        players.forEach {
            val hotbar = it.inventory.query<Hotbar>(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar::class.java))
            hotbar[SlotIndex(0)] = ItemStack.of(ItemTypes.AIR)
            hotbar[SlotIndex(1)] = ItemStack.of(ItemTypes.AIR)
        }

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