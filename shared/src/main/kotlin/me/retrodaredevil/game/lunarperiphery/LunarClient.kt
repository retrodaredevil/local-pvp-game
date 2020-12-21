package me.retrodaredevil.game.lunarperiphery

import me.retrodaredevil.game.lunarperiphery.packet.Packet
import java.util.*

/**
 * The job of this object is to handle the connection to the server, and nothing else.
 */
interface LunarClient {
    fun poll(): Packet?
    fun send(packet: Packet)

    val clientId: UUID
}
