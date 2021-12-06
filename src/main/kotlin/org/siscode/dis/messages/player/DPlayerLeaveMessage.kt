package org.siscode.dis.messages.player

import dev.kord.core.entity.Guild
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import org.siscode.dis.messages.DPlayerUpdateMessage
import org.siscode.dis.style.Stylesheet
import org.siscode.dis.users.DUser
import org.siscode.dis.utils.text.generateText

class DPlayerLeaveMessage(override val sender: DUser) : DPlayerUpdateMessage {
    override suspend fun onlineRender2Kord(guild: Guild): String {
        return offlineRender2Kord()
    }

    override suspend fun onlineRender2MC(guild: Guild): Text {
        val text = sender.generateText(guild)
        text.append(LiteralText(" left ").setStyle(Stylesheet.FALSE))
        text.append(LiteralText("the server."))
        return text
    }

    override fun offlineRender2Kord(): String {
        return "**left the server.**"
    }

    override fun offlineRender2MC(): Text {
        val text = LiteralText("${sender.mcName} ").setStyle(Stylesheet.MISSING)
        text.append(LiteralText("left ").setStyle(Stylesheet.FALSE))
        text.append(LiteralText("the server."))
        return text
    }
}