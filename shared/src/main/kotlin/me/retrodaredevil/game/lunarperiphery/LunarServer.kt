package me.retrodaredevil.game.lunarperiphery

import me.retrodaredevil.game.lunarperiphery.info.InfoMessage
import me.retrodaredevil.game.lunarperiphery.packet.Packet
import java.util.*

/**
 * The job of this object is to handle the connection to the client, and nothing else
 */
interface LunarServer {
    fun pollClientPacket(): Pair<UUID, Packet>?
    fun pollInfoMessage(): InfoMessage?
    fun sendPacket(clientId: UUID, packet: Packet)
    fun broadcast(packet: Packet)
}
