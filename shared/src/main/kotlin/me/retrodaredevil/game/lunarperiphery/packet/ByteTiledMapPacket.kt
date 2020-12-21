package me.retrodaredevil.game.lunarperiphery.packet

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader

class ByteTiledMapPacket(
        val data: ByteArray
) : TiledMapPacket {

    override fun createTiledMap(): TiledMap {
        val fileHandle = Gdx.files.local(".temp.tiled.map.tmx")
        fileHandle.writeBytes(data, false) // TODO this could raise an exception
        return TmxMapLoader { fileHandle }.load("")
    }

}
