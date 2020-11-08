package me.retrodaredevil.game.lunarperiphery.packet

import com.badlogic.gdx.maps.tiled.TiledMap

interface TiledMapPacket : Packet {
    override val packetType: PacketType
        get() = PacketType.TILED_MAP

    fun createTiledMap(): TiledMap
}
