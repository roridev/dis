package org.siscode.dis.utils.text

import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Guild
import net.minecraft.text.HoverEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import org.siscode.dis.style.Stylesheet
import org.siscode.dis.users.DUser
import org.siscode.dis.utils.functional.delegate
import org.siscode.dis.utils.functional.getColoredTopRoleOrNull

suspend fun DUser.toDiscordString(guild: Guild): String {
    return this.delegate("","Server", fromMc = {
        this.mcName ?: ""
    }, fromDiscord = {
        guild.getMember(Snowflake(it.discordId!!)).displayName
    }, linked = {
        guild.getMember(Snowflake(it.discordId!!)).displayName
    })
}

suspend fun DUser.generateText(guild: Guild): MutableText {
    val tooltip = this.generateTooltip(guild)
    val hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, tooltip)
    val text = LiteralText("").setStyle(Stylesheet.EMPTY)
    return this.delegate(LiteralText(""),LiteralText(""), fromMc = {
        text.append(LiteralText(it.mcName).setStyle(Stylesheet.EMPTY.withHoverEvent(hoverEvent)))
    }, fromDiscord = {
        val member = guild.getMember(Snowflake(it.discordId!!))
        val memberstyle = Stylesheet.MEMBERSTYLE(member)
        text.append(LiteralText(member.displayName).setStyle(memberstyle.withHoverEvent(hoverEvent)))
    }, linked = {
        val member = guild.getMember(Snowflake(it.discordId!!))
        val memberstyle = Stylesheet.MEMBERSTYLE(member)
        text.append(LiteralText(it.mcName!!).setStyle(memberstyle.withHoverEvent(hoverEvent)))
    })
}

suspend fun DUser.generateTooltip(guild: Guild) : MutableText {
    val text = LiteralText("").setStyle(Stylesheet.EMPTY)

    suspend fun MUT_getDiscordData(dUser : DUser){
        val member = guild.getMember(Snowflake(dUser.discordId!!))
        val memberstyle = Stylesheet.MEMBERSTYLE(member)
        val memberRole = member.getColoredTopRoleOrNull()
        text.append("Discord: ")
        text.append(LiteralText("@${member.displayName}").setStyle(memberstyle))
        memberRole?.let { text.append(" (${it.name})") }
        text.append("\n")
    }

    return this.delegate(LiteralText(""),LiteralText(""), fromMc = {
        text.append("Minecraft: ")
        text.append(LiteralText(it.mcName).setStyle(Stylesheet.INFO))
        text.append("\n")
        text.append("Status: ")
        text.append(LiteralText("Unlinked\n").setStyle(Stylesheet.FALSE))
        text.append("Tip: ")
        text.append(LiteralText("Use /link").setStyle(Stylesheet.TIP))
    }, fromDiscord = { dUser ->
        MUT_getDiscordData(dUser)
        text.append("Status: ")
        text.append(LiteralText("Unlinked\n").setStyle(Stylesheet.FALSE))
        text.append("Tip: ")
        text.append(LiteralText("Use /link").setStyle(Stylesheet.TIP))
    }, linked = { dUser ->
        MUT_getDiscordData(dUser)
        text.append("Minecraft: ")
        text.append(LiteralText(dUser.mcName).setStyle(Stylesheet.INFO))
        text.append("\n")
        text.append("Status: ")
        text.append(LiteralText("Linked\n").setStyle(Stylesheet.TRUE))
    })
}
