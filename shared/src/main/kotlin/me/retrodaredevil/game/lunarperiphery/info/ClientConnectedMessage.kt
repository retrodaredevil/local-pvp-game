package me.retrodaredevil.game.lunarperiphery.info

import java.util.*

class ClientConnectedMessage(
        val clientId: UUID,
        val isLocal: Boolean
) : InfoMessage {
}
