package org.siscode.dis.messages.player

import dev.kord.core.entity.Guild
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import org.siscode.dis.messages.DMessage
import org.siscode.dis.messages.DPlayerMessage
import org.siscode.dis.style.Stylesheet
import org.siscode.dis.users.DUser
import org.siscode.dis.utils.text.generateText


class DPlayerTextMessage(override val sender: DUser, val content: String) : DPlayerMessage {
    override suspend fun onlineRender2Kord(guild: Guild): String {
        return offlineRender2Kord()
    }

    // Here is where the fancy message interception happens.
    // for *free*.
    override suspend fun onlineRender2MC(guild: Guild): Text {
        val text = LiteralText("<")
        text.append(sender.generateText(guild))
        text.append("> $content")
        return text
    }

    override fun offlineRender2Kord(): String {
        return content
    }

    override fun offlineRender2MC(): Text {
        val text = LiteralText("<")
        text.append(LiteralText(sender.mcName).setStyle(Stylesheet.MISSING))
        text.append("> $content")
        return text
    }
}