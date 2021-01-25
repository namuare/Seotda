package me.mocha.spongeplugin.seotda.util

import me.mocha.spongeplugin.seotda.Seotda
import ninja.leaping.configurate.ConfigurationNode
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

object MessageBuilder {

    val messages: ConfigurationNode

    init {
        val config = YAMLConfigurationLoader.builder().setPath(Seotda.instance.configPath.resolve("message.yml")).build()
        messages = config.load()
    }

    fun pre(key: String, vararg replaces: Pair<String, String>): String {
        var message = messages.getNode(key).getString(key)
        replaces.forEach { message = message.replace("%${it.first}%", it.second) }
        return message
    }

    fun success(key: String, vararg replaces: Pair<String, String>): Text = Text.builder(pre(key, *replaces)).color(TextColors.YELLOW).build()

    fun error(key: String, vararg replaces: Pair<String, String>): Text = Text.builder(pre(key, *replaces)).color(TextColors.RED).build()

}