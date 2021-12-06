package org.siscode.dis.messages.server

import dev.kord.core.entity.Guild
import net.minecraft.text.Text
import org.siscode.dis.messages.DServerMessage

class DUnsuportedMessage (val text: Text) : DServerMessage {
    override suspend fun onlineRender2Kord(guild: Guild): String {
        return offlineRender2Kord()
    }

    override suspend fun onlineRender2MC(guild: Guild): Text {
        return offlineRender2MC()
    }

    override fun offlineRender2Kord(): String {
        return "Unsupported Message : `${text.javaClass.name}`\n" +
                "```json\n" +
                Text.Serializer.toJson(text) +
                "```"
    }

    override fun offlineRender2MC(): Text {
        return text
    }

}