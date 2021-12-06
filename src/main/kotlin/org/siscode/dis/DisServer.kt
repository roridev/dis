package org.siscode.dis

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.network.MessageType
import net.minecraft.text.Text
import net.minecraft.util.Util
import org.siscode.dis.config.Config
import org.siscode.dis.kord.Bot
import org.siscode.dis.messages.DMessage
import org.siscode.dis.messages.player.DPlayerJoinMessage
import org.siscode.dis.messages.player.DPlayerLeaveMessage
import org.siscode.dis.messages.server.DServerClosedMessage
import org.siscode.dis.messages.server.DServerOpenedMessage
import org.siscode.dis.text.TaggedText
import org.siscode.dis.utils.functional.getUUIDorNIL
import org.siscode.dis.utils.io.createNewConfigIfMissingOrReadExisitingConfig
import org.siscode.dis.utils.io.getOrCreateByUUID
import org.siscode.dis.utils.io.saveConfig
import java.util.*

class DisServer : DedicatedServerModInitializer {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onInitializeServer() {
        val job = GlobalScope.launch {
            println("Kord job started")
            val bot = Bot(
                minecraftSource = MCIn,
                minecraftSink = MCOut,
                processedKord = ProcessedKord,
                processedMC = ProcessedMC
            )

            bot.init()
        }

//        ServerLifecycleEvents.SERVER_STARTED.register {
//            ProcessedKord.trySend(DServerOpenedMessage())
//            ProcessedMC.trySend(DServerOpenedMessage())
//        }
//
        ServerLifecycleEvents.SERVER_STOPPING.register {
            // ProcessedKord.trySend(DServerClosedMessage())
            // ProcessedMC.trySend(DServerClosedMessage())
            println("Saving config.")
            saveConfig("./mods/dis.config.json", CONFIG)
            println("Saved config.")
            val linked = CONFIG.data.filter { it.isLinked() }.size
            val discord = CONFIG.data.filterNot { it.isLinked() }.filter { it.hasDiscordLink() }.size
            val mc = CONFIG.data.filterNot { it.isLinked() }.filter { it.hasMcLink() }.size
            println("$linked Linked players | $discord Discord-only users | $mc MC-only players")
        }

        ServerTickEvents.START_SERVER_TICK.register {
            var item = MCOut.tryReceive()
            while (item.isSuccess) {
                val text = item.getOrNull()!!
                println("Got ${text.javaClass.name}.")
                it.playerManager.broadcast(text.data, MessageType.CHAT, text.sender.getUUIDorNIL())
                item = MCOut.tryReceive()
            }
        }

        ServerPlayConnectionEvents.JOIN.register { spnh, _, _ ->
            val user = CONFIG.getOrCreateByUUID(spnh.player.uuid, spnh.player.entityName)
            val message = DPlayerJoinMessage(user)
            ProcessedKord.trySend(message)
            ProcessedMC.trySend(message)
        }

        ServerPlayConnectionEvents.DISCONNECT.register {spnh,_ ->
            val user = CONFIG.getOrCreateByUUID(spnh.player.uuid, spnh.player.entityName)
            val message = DPlayerLeaveMessage(user)
            ProcessedKord.trySend(message)
            ProcessedMC.trySend(message)
        }

    }

    companion object {
        var CONFIG: Config = createNewConfigIfMissingOrReadExisitingConfig("./mods/dis.config.json")

        val MCIn: Channel<Text> = Channel(Channel.Factory.UNLIMITED)
        val MCOut: Channel<TaggedText> = Channel(Channel.Factory.UNLIMITED)
        val ProcessedKord: Channel<DMessage> = Channel(Channel.Factory.UNLIMITED)
        val ProcessedMC: Channel<DMessage> = Channel(Channel.Factory.UNLIMITED)
    }
}