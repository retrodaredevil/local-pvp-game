package me.retrodaredevil.game.lunarperiphery.packet

import java.util.*

/**
 * When sent from client to server, it is the client updating its location. When sent from server to client, it is the server teleporting the player
 */
class PlayerLocationPacket(
        val x: Float,
        val y: Float,
        val movementId: UUID
) : Packet {
    override val packetType: PacketType
        get() = PacketType.PLAYER_LOCATION
}
