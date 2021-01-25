package me.mocha.spongeplugin.seotda

import com.google.inject.Inject
import me.mocha.spongeplugin.seotda.command.SeotdaCommand
import me.mocha.spongeplugin.seotda.service.SeotdaGameService
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.config.ConfigDir
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.event.game.state.GamePostInitializationEvent
import org.spongepowered.api.event.game.state.GamePreInitializationEvent
import org.spongepowered.api.plugin.Dependency
import org.spongepowered.api.plugin.Plugin
import java.nio.file.Files
import java.nio.file.Path

@Plugin(
    id = "seotda",
    name = "Seotda",
    version = "1.0-SNAPSHOT",
    description = "korean game \"Seotda\" for sponge api",
    dependencies = [
        Dependency(id = "kponge", version = "1.3.72")
    ]
)
class Seotda {

    @Inject
    lateinit var logger: Logger

    @Inject
    @ConfigDir(sharedRoot = false)
    lateinit var configPath: Path

    companion object {
        lateinit var instance: Seotda
            private set
    }

    init {
        instance = this
    }

    @Listener
    fun onPreInit(event: GamePreInitializationEvent) {
        if (Files.notExists(configPath)) Files.createDirectories(configPath)
        Sponge.getAssetManager().getAsset(this, "config.yml").get().copyToFile(configPath.resolve("config.yml"))
        Sponge.getAssetManager().getAsset(this, "message.yml").get().copyToFile(configPath.resolve("message.yml"))
    }

    @Listener
    fun onInit(event: GameInitializationEvent) {
        Sponge.getEventManager().registerListeners(this, EventListener)
        Sponge.getCommandManager().register(this, SeotdaCommand.spec, "seotda")
    }

    @Listener
    fun onPostInit(event: GamePostInitializationEvent) {
        Sponge.getServiceManager().setProvider(this, SeotdaGameService::class.java, SeotdaGameService)
    }

}