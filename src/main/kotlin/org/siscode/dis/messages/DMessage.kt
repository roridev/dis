package org.siscode.dis.messages

import dev.kord.core.entity.Guild
import net.minecraft.text.Text
import org.siscode.dis.users.DUser

interface DMessage {
    val sender: DUser

    suspend fun render2Kord(guild: Guild?) : String {
        return if (guild == null){
            offlineRender2Kord()
        } else {
            onlineRender2Kord(guild)
        }
    }

    suspend fun render2MC(guild: Guild?) : Text {
        return if (guild == null) {
            offlineRender2MC()
        } else {
            onlineRender2MC(guild)
        }
    }

    suspend fun onlineRender2Kord(guild: Guild): String
    suspend fun onlineRender2MC(guild: Guild): Text
    fun offlineRender2Kord(): String
    fun offlineRender2MC(): Text
}