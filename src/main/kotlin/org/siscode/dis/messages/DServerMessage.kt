package org.siscode.dis.messages

import org.siscode.dis.users.DUser

interface DServerMessage : DMessage {
    override val sender: DUser
        get() = DUser.SERVER
}