package me.retrodaredevil.game.lunarperiphery

import me.retrodaredevil.game.lunarperiphery.info.ClientConnectedMessage
import me.retrodaredevil.game.lunarperiphery.info.InfoMessage
import me.retrodaredevil.game.lunarperiphery.packet.Packet
import java.lang.IllegalStateException
import java.util.*

class BasicClientServerBridge(
        override val clientId: UUID
) : LunarClient, LunarServer {
    private val clientQueue: Queue<Packet> = LinkedList()
    private val serverQueue: Queue<Packet> = LinkedList()

    private val serverInfoQueue: Queue<InfoMessage> = LinkedList()

    init {
        serverInfoQueue.add(ClientConnectedMessage(clientId, true))
    }

    override fun poll(): Packet? {
        return clientQueue.poll()
    }

    override fun send(packet: Packet) {
        serverQueue.add(packet)
    }

    override fun pollClientPacket(): Pair<UUID, Packet>? {
        val r = serverQueue.poll() ?: return null
        return Pair(clientId, r)
    }

    override fun pollInfoMessage(): InfoMessage? {
        return serverInfoQueue.poll()
    }

    override fun sendPacket(clientId: UUID, packet: Packet) {
        if (clientId != this.clientId) {
            throw IllegalStateException("That client is not connected! client: $clientId")
        }
        clientQueue.add(packet)
    }

    override fun broadcast(packet: Packet) {
        clientQueue.add(packet)
    }
}
