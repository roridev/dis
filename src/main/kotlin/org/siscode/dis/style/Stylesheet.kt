package org.siscode.dis.style

import dev.kord.core.entity.Member
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.toList
import net.minecraft.text.Style
import net.minecraft.util.Formatting
import org.siscode.dis.utils.functional.getColoredTopRoleOrNull

class Stylesheet {
    companion object {
        val EMPTY : Style = Style.EMPTY
        val BLURPLE : Style = EMPTY.withColor(0x7289DA)
        val FALSE : Style = EMPTY.withColor(Formatting.RED)
        val INFO : Style = EMPTY.withColor(Formatting.GRAY)
        val TIP : Style = EMPTY.withColor(Formatting.GOLD).withFormatting(Formatting.ITALIC)
        val TRUE : Style = EMPTY.withColor(Formatting.GREEN)
        val MISSING: Style = BLURPLE.italic()

        suspend fun MEMBERSTYLE (m : Member) : Style {
            val toprole = m.getColoredTopRoleOrNull()
            return EMPTY.withColor(toprole?.color?.rgb ?: 0xFFFFFF)
        }
    }
}

fun Style.bold () : Style {
    return this.withFormatting(Formatting.BOLD)
}

fun Style.italic () : Style {
    return this.withFormatting(Formatting.ITALIC)
}

fun Style.underline () : Style {
    return this.withFormatting(Formatting.UNDERLINE)
}

fun Style.strikethrough(): Style {
    return this.withFormatting(Formatting.STRIKETHROUGH)
}

fun Style.obfuscated() : Style {
    return this.withFormatting(Formatting.OBFUSCATED)
}

fun Style.resetFormatting() : Style {
    return this.withFormatting(Formatting.RESET)
}

