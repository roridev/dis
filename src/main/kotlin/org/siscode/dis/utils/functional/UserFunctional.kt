package org.siscode.dis.utils.functional

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import dev.kord.core.entity.Member
import net.minecraft.command.argument.UuidArgumentType.uuid
import net.minecraft.util.Util
import org.siscode.dis.users.DUser
import java.util.*

suspend fun <A> DUser.delegate(
    default: A, server: A, fromMc: suspend (DUser) -> A, fromDiscord: suspend (DUser) -> A, linked: suspend (DUser) -> A
): A {
    if (this.isServer()) return server
    if (this.isLinked()) {
        return linked(this)
    } else {
        if (this.hasDiscordLink()) {
            return fromDiscord(this)
        }
        if (this.hasMcLink()) {
            return fromMc(this)
        }
    }
    return default
}

fun <A> DUser.delegateStrict(
    default: A, server: A, fromMc: (DUser) -> A, fromDiscord: (DUser) -> A, linked: (DUser) -> A
): A {
    if (this.isServer()) return server
    if (this.isLinked()) {
        return linked(this)
    } else {
        if (this.hasDiscordLink()) {
            return fromDiscord(this)
        }
        if (this.hasMcLink()) {
            return fromMc(this)
        }
    }
    return default
}


suspend fun DUser.getLinkedDiscordMemberOrNull(guild: Guild): Member? {
    return this.delegate(null, null, fromMc = {
        null
    }, fromDiscord = {
        guild.getMember(Snowflake(it.discordId!!))
    }, linked = {
        guild.getMember(Snowflake(it.discordId!!))
    })
}

fun DUser.getUUIDorNIL(): UUID {
    return this.delegateStrict(Util.NIL_UUID, Util.NIL_UUID,fromMc = {
        it.uuid!!
    }, fromDiscord = {
       Util.NIL_UUID
    }, linked = {
        it.uuid!!
    })
}