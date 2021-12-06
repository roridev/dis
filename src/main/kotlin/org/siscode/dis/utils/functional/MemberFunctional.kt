package org.siscode.dis.utils.functional

import dev.kord.core.entity.Icon
import dev.kord.core.entity.Member
import dev.kord.core.entity.Role
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.toList

suspend fun Member.getColoredTopRoleOrNull() : Role? {
    return this.roles.filterNot { it.color.rgb == 0x0 }.toList().maxOrNull()
}

fun Member.getAnyAvatar() : Icon {
    return this.memberAvatar ?: this.avatar ?: this.defaultAvatar
}