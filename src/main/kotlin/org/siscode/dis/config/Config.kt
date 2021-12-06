package org.siscode.dis.config

import kotlinx.serialization.Serializable
import org.siscode.dis.users.DUser

@Serializable
data class Config(val webhook: WebhookConfig, val bot:BotConfig, val channels: ChannelConfig ,val data: MutableList<DUser>)

@Serializable
data class WebhookConfig(val id: ULong, val token: String)

@Serializable
data class BotConfig(val token: String)

@Serializable data class ChannelConfig(val guildId: ULong, val bridgeId: ULong)