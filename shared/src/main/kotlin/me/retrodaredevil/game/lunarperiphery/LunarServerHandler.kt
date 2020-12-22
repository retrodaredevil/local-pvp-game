package me.retrodaredevil.game.lunarperiphery

import me.retrodaredevil.game.lunarperiphery.info.ClientConnectedMessage
import me.retrodaredevil.game.lunarperiphery.packet.LocalTiledMapPacket
import me.retrodaredevil.game.lunarperiphery.packet.PlayerLocationPacket
import me.retrodaredevil.game.lunarperiphery.packet.RequestTiledMapPacket
import java.util.*

class LunarServerHandler(
        private val server: LunarServer
) : Updatable {
    private val localClients = mutableListOf<UUID>()
    override fun update(delta: Float) {
        while (true) {
            val message = server.pollInfoMessage() ?: break
            when (message) {
                is ClientConnectedMessage -> {
                    if (message.isLocal) {
                        localClients.add(message.clientId)
                    }
                    println("${message.clientId} has connected!")
                }
            }
        }
        while (true) {
            val (clientId, packet) = server.pollClientPacket() ?: break
            when (packet) {
                is RequestTiledMapPacket -> {
                    val tiledMapPacket = if (clientId in localClients) LocalTiledMapPacket("untitled.tmx") else error("Not supported yet!")
                    server.sendPacket(clientId, tiledMapPacket)
                    server.sendPacket(clientId, PlayerLocationPacket(59f, 55f, UUID.randomUUID()))
                }
                is PlayerLocationPacket -> {
                    println("Player is at ${packet.x}, ${packet.y}")
                }
            }
        }
    }
}
