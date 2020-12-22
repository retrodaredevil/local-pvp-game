package me.retrodaredevil.game.lunarperiphery.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import me.retrodaredevil.game.lunarperiphery.GameMap
import me.retrodaredevil.game.lunarperiphery.LunarClient
import me.retrodaredevil.game.lunarperiphery.Updatable
import me.retrodaredevil.game.lunarperiphery.packet.PlayerLocationPacket
import me.retrodaredevil.game.lunarperiphery.packet.RequestTiledMapPacket
import me.retrodaredevil.game.lunarperiphery.packet.TiledMapPacket
import me.retrodaredevil.game.lunarperiphery.render.*

class GameScreen(
        private val client: LunarClient,
        private val updatable: Updatable
) : ScreenAdapter() {

    private val renderable: Renderable

    private var gameMap: GameMap? = null

    init {
        renderable = RenderableMultiplexer(listOf(ResetRenderable(Color.BLACK), RenderableReference(::gameMap)))
        client.send(RequestTiledMapPacket())
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun resize(width: Int, height: Int) {
        renderable.resize(width, height)
    }


    override fun render(delta: Float) {
        updatable.update(delta) // usually makes sure the client/server is up to date
        while (true) {
            val packet = client.poll() ?: break
            when (packet) {
                is TiledMapPacket -> {
                    val tiledMap = packet.createTiledMap()
                    gameMap = GameMap(tiledMap)
                }
                is PlayerLocationPacket -> {
                    val gameMap = gameMap
                    if (gameMap != null) {
                        gameMap.teleport(packet.x, packet.y, packet.movementId)
                    } else {
                        println("gameMap is null!")
                    }
                }
            }
        }
        gameMap?.let { gameMap ->
            gameMap.update(delta)
            client.send(PlayerLocationPacket(gameMap.playerBody.position.x, gameMap.playerBody.position.y, gameMap.movementId))
        }
        renderable.render(delta)
    }

    override fun dispose() {
        renderable.dispose()
    }
}
