package me.retrodaredevil.game.lunarperiphery.packet

class RequestTiledMapPacket : Packet {
    override val packetType: PacketType
        get() = PacketType.REQUEST_TILED_MAP
}
