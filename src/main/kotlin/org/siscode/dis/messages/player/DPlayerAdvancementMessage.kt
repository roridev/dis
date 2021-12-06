package org.siscode.dis.messages.player

import dev.kord.core.entity.Guild
import net.minecraft.text.Text
import org.siscode.dis.messages.DPlayerMessage
import org.siscode.dis.users.DUser

class DPlayerAdvancementMessage(override val sender: DUser) : DPlayerMessage {
    override suspend fun onlineRender2Kord(guild: Guild): String {
        TODO("Not yet implemented")
    }

    override suspend fun onlineRender2MC(guild: Guild): Text {
        TODO("Not yet implemented")
    }

    override fun offlineRender2Kord(): String {
        TODO("Not yet implemented")
    }

    override fun offlineRender2MC(): Text {
        TODO("Not yet implemented")
    }
}