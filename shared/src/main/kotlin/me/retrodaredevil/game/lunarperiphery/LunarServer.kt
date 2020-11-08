package me.retrodaredevil.game.lunarperiphery

import me.retrodaredevil.game.lunarperiphery.packet.Packet

interface LunarServer {
    fun poll(): Packet?
}
