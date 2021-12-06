package org.siscode.dis.processor

import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.execute
import dev.kord.core.behavior.getChannelOf
import dev.kord.core.entity.channel.GuildMessageChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import org.siscode.dis.DisServer
import org.siscode.dis.messages.DMessage
import org.siscode.dis.messages.DServerMessage
import org.siscode.dis.messages.DPlayerUpdateMessage
import org.siscode.dis.messages.player.DPlayerAdvancementMessage
import org.siscode.dis.messages.player.DPlayerTextMessage
import org.siscode.dis.utils.functional.getAnyAvatar
import org.siscode.dis.utils.functional.getLinkedDiscordMemberOrNull
import org.siscode.dis.utils.text.toDiscordString

class KordProcessor (val processedKordSource : Channel<DMessage>) : Extension() {
    override val name: String
        get() = "KORDp - Kord Processor"

    override suspend fun setup() {
        println("KORDp started")
        val guild = kord.getGuild(Snowflake(DisServer.CONFIG.channels.guildId))
        val channel = guild?.getChannelOf<GuildMessageChannel>(Snowflake(DisServer.CONFIG.channels.bridgeId))

        val webhook = kord.getWebhookWithToken(Snowflake(DisServer.CONFIG.webhook.id), DisServer.CONFIG.webhook.token)
        
        kord.launch {
            processedKordSource.consumeEach {
                val rendered = it.render2Kord(guild)
                val senderText = guild?.let {g -> it.sender.toDiscordString(g)} ?: it.sender.mcName

                if (it is DPlayerUpdateMessage) {
                    channel?.createMessage("***$senderText*** $rendered")
                }

                if (it is DServerMessage) {
                    channel?.createMessage(rendered)
                }

                if (it is DPlayerTextMessage || it is DPlayerAdvancementMessage) {
                    val memberOrNull = guild?.let { g ->  it.sender.getLinkedDiscordMemberOrNull(g)}
                    webhook.execute(DisServer.CONFIG.webhook.token) {
                        this.username = senderText
                        this.content = rendered
                        memberOrNull?.let { member -> this.avatarUrl = member.getAnyAvatar().url }
                    }
                }
            }
        }
    }
}