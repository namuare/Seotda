package me.mocha.spongeplugin.seotda.util

import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

object TextBuilder {

    fun success(message: String): Text {
        return Text.builder(message).color(TextColors.YELLOW).build()
    }

    fun error(message: String): Text {
        return Text.builder(message).color(TextColors.RED).build()
    }

}