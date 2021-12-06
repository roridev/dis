package org.siscode.dis.utils.functional

import kotlinx.coroutines.channels.Channel

fun <T> tryGet(ch: Channel<T>) : T? {
    return ch.tryReceive().getOrNull()
}

fun <T> trySend(ch: Channel<T>, m : T) : Boolean {
    val trySend = ch.trySend(m)
    return trySend.isSuccess
}