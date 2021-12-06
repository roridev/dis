package org.siscode.dis.users

import kotlinx.serialization.Serializable
import org.siscode.dis.utils.serialization.UUIDSerializer
import java.util.*

@Serializable
class DUser(
    @Serializable(with = UUIDSerializer::class)
    var uuid: UUID?, var discordId: ULong?, var mcName: String?, var discordName: String?) {

    fun isLinked () : Boolean {
        return uuid != null && discordId != null
    }

    fun hasDiscordLink () : Boolean {
        return discordId != null
    }

    fun hasMcLink() : Boolean {
        return uuid != null
    }

    fun isServer() : Boolean {
        return this == SERVER
    }

    companion object {
        var SERVER : DUser = DUser(null, null, "!#_SERVER_#!", "!#_SERVER_#!")
    }
}