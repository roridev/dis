package org.siscode.dis.processor

import com.kotlindiscord.kord.extensions.extensions.Extension
import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import net.minecraft.text.Text
import org.siscode.dis.DisServer
import org.siscode.dis.messages.DMessage
import org.siscode.dis.text.TaggedText

class MCProcessor(
    val processedMCSource: Channel<DMessage>,
    val minecraftSink: Channel<TaggedText>
) : Extension(){
    override val name: String
        get() = "MCp - Minecraft Processor"

    override suspend fun setup() {
        println("MCp started")
        val guild = kord.getGuild(Snowflake(DisServer.CONFIG.channels.guildId))

        kord.launch {
            processedMCSource.consumeEach {
               minecraftSink.send(TaggedText(it.render2MC(guild), it.sender))
            }
        }
    }

}