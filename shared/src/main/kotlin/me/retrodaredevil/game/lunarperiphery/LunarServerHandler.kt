package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Gdx
import me.retrodaredevil.game.lunarperiphery.entity.Player
import me.retrodaredevil.game.lunarperiphery.info.ClientConnectedMessage
import me.retrodaredevil.game.lunarperiphery.packet.ByteTiledMapPacket
import me.retrodaredevil.game.lunarperiphery.packet.LocalTiledMapPacket
import me.retrodaredevil.game.lunarperiphery.packet.PlayerLocationPacket
import me.retrodaredevil.game.lunarperiphery.packet.RequestTiledMapPacket
import java.util.*

class LunarServerHandler(
        private val server: LunarServer
) : Updatable {
    private val playerMap = mutableMapOf<UUID, Player>()
    private val tiledMapBytesMap = mutableMapOf<String, ByteArray>()

    private fun sendTiledMap(player: Player, fileName: String) {
        val tiledMapPacket = if (player.isLocal) LocalTiledMapPacket(fileName) else {
            val fileHandle = Gdx.files.internal(fileName)
            val bytes = fileHandle.readBytes() // TODO this could throw an exception
            ByteTiledMapPacket(bytes)
        }
        server.sendPacket(player.id, tiledMapPacket)
    }
    private fun teleportPlayer(player: Player, x: Float, y: Float) {
        val movementId = UUID.randomUUID()
        player.movementId = movementId
        server.sendPacket(player.id, PlayerLocationPacket(x, y, movementId))
    }

    override fun update(delta: Float) {
        while (true) {
            val message = server.pollInfoMessage() ?: break
            when (message) {
                is ClientConnectedMessage -> {
                    playerMap[message.clientId] = Player(message.clientId, message.isLocal)
                    println("${message.clientId} has connected!")
                }
            }
        }
        while (true) {
            val (clientId, packet) = server.pollClientPacket() ?: break
            val player = playerMap[clientId] ?: error("This player doesn't exist! clientId: $clientId")
            when (packet) {
                is RequestTiledMapPacket -> {
                    sendTiledMap(player, "untitled.tmx")
                    teleportPlayer(player, 59f, 55f)
                }
                is PlayerLocationPacket -> {
                    if (packet.movementId == player.movementId) {
                        player.setLocation(packet.x, packet.y)
//                        println("Player is at ${player.location}")
                    } else {
                        println("Received old movement packet")
                    }
                }
            }
        }
    }
}
