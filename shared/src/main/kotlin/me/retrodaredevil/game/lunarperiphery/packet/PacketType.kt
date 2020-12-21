package me.retrodaredevil.game.lunarperiphery.packet

enum class PacketType(
        val clientToServerAllowed: Boolean = true,
        val serverToClientAllowed: Boolean = true
) {
    TILED_MAP(clientToServerAllowed = false),
    REQUEST_TILED_MAP(serverToClientAllowed = false)
    ;

}
