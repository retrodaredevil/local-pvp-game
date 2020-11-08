package me.retrodaredevil.game.lunarperiphery.packet

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

class LocalTiledMapPacket(
        val fileName: String
) : TiledMapPacket {
    override fun createTiledMap(): TiledMap {
        return TmxMapLoader().load(fileName)
    }
}
