package org.siscode.dis.messages.server

import dev.kord.core.entity.Guild
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import org.siscode.dis.messages.DServerMessage
import org.siscode.dis.style.Stylesheet
import org.siscode.dis.style.bold

class DServerClosedMessage : DServerMessage {
    override suspend fun onlineRender2Kord(guild: Guild) : String {
       return offlineRender2Kord()
    }

    override suspend fun onlineRender2MC(guild: Guild): Text {
        return offlineRender2MC()
    }

    override fun offlineRender2Kord(): String {
        return "\uD83D\uDFE5 **Server is closing.**"
    }

    override fun offlineRender2MC(): Text {
        val text = LiteralText("[")
        text.append(LiteralText("SERVER").setStyle(Stylesheet.INFO))
        text.append(LiteralText("] ").setStyle(Stylesheet.EMPTY))
        text.append(LiteralText(" The server is now ").setStyle(Stylesheet.TIP))
        text.append(LiteralText("CLOSING").setStyle(Stylesheet.FALSE.bold()))
        return text
    }
}