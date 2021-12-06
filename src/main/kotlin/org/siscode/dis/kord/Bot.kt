package org.siscode.dis.kord

import com.kotlindiscord.kord.extensions.ExtensibleBot
import dev.kord.common.entity.PresenceStatus
import dev.kord.gateway.Intents
import kotlinx.coroutines.channels.Channel
import net.minecraft.text.Text
import org.siscode.dis.DisServer
import org.siscode.dis.messages.DMessage
import org.siscode.dis.processor.KordProcessor
import org.siscode.dis.processor.MCProcessor
import org.siscode.dis.processor.TextProcessor
import org.siscode.dis.text.TaggedText

class Bot(
    val minecraftSource: Channel<Text>,
    val minecraftSink: Channel<TaggedText>,
    val processedMC: Channel<DMessage>,
    val processedKord: Channel<DMessage>
) {
    suspend fun init() {
        val bot = ExtensibleBot(DisServer.CONFIG.bot.token) {

            extensions {
                add { TextProcessor(minecraftSource = minecraftSource, processedMinecraftSink = processedMC, processedKordSink = processedKord) }
                add { KordProcessor(processedKordSource = processedKord)}
                add { MCProcessor(processedMCSource = processedMC,minecraftSink = minecraftSink)}
            }

            presence {
                afk = false
                since = kotlinx.datetime.Clock.System.now()
                status = PresenceStatus.Online
                playing("Minecraft")
            }
        }

        bot.start()
    }

}