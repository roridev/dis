package org.siscode.dis.kord

import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.core.event.gateway.ConnectEvent
import dev.kord.core.event.gateway.DisconnectEvent
import dev.kord.core.event.gateway.ReadyEvent
import dev.kord.core.on

class ConnectionHandler : Extension() {
    override val name: String
        get() = "Connection Handler"

    override suspend fun setup() {
        kord.on<ConnectEvent> {
            println("Bot connected")
        }

        kord.on<ReadyEvent> {
            println("Bot ready")
        }

        kord.on<DisconnectEvent> {
            println("Bot disconnected. Attempting to reconnect")
            kord.login()
        }
    }
}