package org.siscode.dis.utils.io

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.siscode.dis.config.BotConfig
import org.siscode.dis.config.ChannelConfig
import org.siscode.dis.config.Config
import org.siscode.dis.config.WebhookConfig
import org.siscode.dis.users.DUser
import java.io.File
import java.util.*

fun readConfig(path: String) : Config {
    val text = File(path).readText()
    return Json.decodeFromString(text)
}

fun saveConfig(path: String, config: Config) {
    val jsonString = Json{prettyPrint=true}.encodeToString(config)
    File(path).writeText(jsonString)
}

fun createNewConfigIfMissing(path: String) {
    val file = File(path)
    if(file.exists()) return
    file.createNewFile()
    saveConfig(path, Config(WebhookConfig(0u, "Please put your WEBHOOK token here."), BotConfig("Please put your BOT token here"),
       ChannelConfig(0u,0u), mutableListOf()
    ))
}

fun createNewConfigIfMissingOrReadExisitingConfig(path: String) : Config {
    val file = File(path)
    if (!file.exists()) {
        createNewConfigIfMissing(path)
    }
    return readConfig(path)
}

fun Config.getMemberByUUIDOrNull(u : UUID) : DUser? {
    return this.data.find { it.uuid == u }
}

fun Config.getMemberByDiscordIDOrNull(u : ULong) : DUser? {
    return this.data.find { it.discordId == u }
}

fun Config.getOrCreateByUUID(u : UUID, mcName: String) : DUser {
    return this.getMemberByUUIDOrNull(u) ?:  this.append(DUser(u, null, mcName, ""))
}

fun Config.getOrCreateByDiscordId(u : ULong, discordName: String) : DUser {
    return this.getMemberByDiscordIDOrNull(u) ?:  this.append(DUser(null, u, "", discordName))
}

fun Config.update(key: UUID, userUpdateBuilder : (DUser) -> Unit) : DUser? {
    val dUser = this.getMemberByUUIDOrNull(key) ?: return null
    this.data.remove(dUser)
    userUpdateBuilder(dUser)
    this.data.add(dUser)
    return dUser
}

fun Config.update(key: ULong, userUpdateBuilder : (DUser) -> Unit) : DUser? {
    val dUser = this.getMemberByDiscordIDOrNull(key) ?: return null
    this.data.remove(dUser)
    userUpdateBuilder(dUser)
    this.data.add(dUser)
    return dUser
}

fun Config.merge(discordKey: ULong, minecraftKey: UUID) : DUser? {
    val a = this.getMemberByDiscordIDOrNull(discordKey)
    if (a != null) {
        return this.update(minecraftKey) {
            it.discordId = a.discordId
            it.discordName = a.discordName
        }
    }
    val b = this.getMemberByUUIDOrNull(minecraftKey)
    if (b != null) {
        return this.update(discordKey) {
            it.uuid = b.uuid
            it.mcName = b.mcName
        }
    }
    return null
}

fun Config.append(user: DUser) : DUser {
    this.data.add(user)
    println("New user added : $user")
    return user
}