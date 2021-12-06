package org.siscode.dis.messages.discord

import dev.kord.core.entity.Guild
import kotlinx.serialization.json.JsonNull.content
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.siscode.dis.messages.DMessage
import org.siscode.dis.style.Stylesheet
import org.siscode.dis.users.DUser
import org.siscode.dis.utils.text.generateText

class DDiscordMessage(override val sender: DUser,val content: String) : DMessage {
    override suspend fun onlineRender2Kord(guild: Guild): String {
        throw UnsupportedOperationException("This should *never* be called." +
                "There is no need to render a discord message back to discord since this configures a loop-back.")
    }

    override suspend fun onlineRender2MC(guild: Guild): Text {
        val text = LiteralText("<")
        text.append(sender.generateText(guild))
        text.append("> $content")
        return text
    }

    override fun offlineRender2Kord(): String {
        throw UnsupportedOperationException("This should *never* be called." +
                "There is no need to render a discord message back to discord since this configures a loop-back.")
    }

    override fun offlineRender2MC(): Text {
        val text = LiteralText("<")
        text.append(LiteralText(sender.mcName).setStyle(Stylesheet.MISSING))
        text.append("> $content")
        return text
    }
}