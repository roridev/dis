package org.siscode.dis.processor

import com.google.gson.JsonObject
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.event
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.channel.GuildMessageChannel
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import org.siscode.dis.DisServer
import org.siscode.dis.messages.DMessage
import org.siscode.dis.messages.discord.DDiscordMessage
import org.siscode.dis.messages.player.DPlayerTextMessage
import org.siscode.dis.messages.server.DUnsuportedMessage
import org.siscode.dis.users.DUser
import org.siscode.dis.utils.io.getMemberByDiscordIDOrNull
import org.siscode.dis.utils.io.getOrCreateByDiscordId
import org.siscode.dis.utils.io.getOrCreateByUUID
import java.util.*

class TextProcessor(
    val minecraftSource: Channel<Text>,
    val processedMinecraftSink: Channel<DMessage>,
    val processedKordSink: Channel<DMessage>
) : Extension() {
    override val name: String
        get() = "TEXTp - Text processing engine"

    override suspend fun setup() {
        println("TEXTp Started")

        //Kord Source
        event<MessageCreateEvent> {
            println("Created Discord Source")
            action {
                val discordMessage = event.message
                if (discordMessage.channel.id.value != DisServer.CONFIG.channels.bridgeId) return@action
                if (discordMessage.author == null || discordMessage.author?.isBot == true) return@action
                println("Got valid message.")
                val id = discordMessage.author!!.id.value
                val name = event.member?.displayName ?: discordMessage.author!!.username
                val user = DisServer.CONFIG.getOrCreateByDiscordId(id, name)
                val message = DDiscordMessage(user, discordMessage.content)
                processedMinecraftSink.send(message)
                println("Message sent to MCp")
            }
        }

        //Minecraft Source
        kord.launch {
            minecraftSource.consumeEach {
                //This text WILL be anything
                //For now we will consume only player messages and call it a day
                if (it is TranslatableText && it.key == "chat.type.text") {
                    val contents = it.args.last() as String
                    val obj = it.args.first() as LiteralText
                    val name = obj.rawString
                    val hoverEvent = obj.style.hoverEvent
                    val action = hoverEvent?.action as HoverEvent.Action<*>
                    val hoverAction = hoverEvent.getValue(action)

                    if (hoverAction is HoverEvent.EntityContent) {
                        val user = DisServer.CONFIG.getOrCreateByUUID(hoverAction.uuid, name)
                        val message = DPlayerTextMessage(user, contents)
                        processedMinecraftSink.send(message)
                        processedKordSink.send(message)
                    }
                } else {
                    val message = DUnsuportedMessage(it)
                    processedMinecraftSink.send(message)
                    processedKordSink.send(message)
                }
            }
        }
    }
}