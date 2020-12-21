package me.retrodaredevil.game.lunarperiphery.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import me.retrodaredevil.game.lunarperiphery.LunarClient
import me.retrodaredevil.game.lunarperiphery.Updatable
import me.retrodaredevil.game.lunarperiphery.packet.RequestTiledMapPacket
import me.retrodaredevil.game.lunarperiphery.packet.TiledMapPacket
import me.retrodaredevil.game.lunarperiphery.render.*

class GameScreen(
        private val client: LunarClient,
        private val updatable: Updatable
) : ScreenAdapter() {

    private val renderable: Renderable
    private val tiledMapRenderableChooser = RenderableChooser()

    private val camera = OrthographicCamera()
    private val viewport = ExtendViewport(640.0f, 480.0f, camera)

    init {
        renderable = RenderableMultiplexer(listOf(ResetRenderable(Color.BLACK), tiledMapRenderableChooser))
        client.send(RequestTiledMapPacket())
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun render(delta: Float) {
        updatable.update(delta)
        val vector = Vector2()
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            vector.y = 1f
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            vector.y = -1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            vector.x = 1f
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            vector.x = -1f
        }
        if (!vector.isZero) {
            vector.nor().scl(delta * 10)
        }
        println(vector)
        viewport.camera.translate(vector.x, vector.y, 0f)
        while (true) {
            val packet = client.poll() ?: break
            when (packet) {
                is TiledMapPacket -> {
                    val tiledMap = packet.createTiledMap()
                    tiledMapRenderableChooser.renderable = TiledMapRenderable(tiledMap, camera)
                }
            }
        }
        renderable.render(delta)
    }
}
