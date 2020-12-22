package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import me.retrodaredevil.game.lunarperiphery.render.*
import java.util.*

class GameMap(
        private val tiledMap: TiledMap
) : Updatable, Renderable {
    val tileSize = tiledMap.properties["tilewidth"] as Int
    private val tiledViewport: Viewport
    private val stageViewport: Viewport
    private val stage: Stage

    private val renderable: Renderable

    val playerLocation = Vector2()
    var movementId = UUID.randomUUID()
    private val tempVector = Vector2()

    init {
        val tiledCamera = OrthographicCamera()
        tiledViewport = ExtendViewport(tileSize * 20f, tileSize * 15f, tiledCamera)

        stageViewport = ExtendViewport(20f, 15f)
        stage = Stage(stageViewport)

        var backgroundLayer: Int? = null
        var floorLayer: Int? = null
        var foregroundLayer: Int? = null
        var topLayer: Int? = null
        for ((i, layer) in tiledMap.layers.withIndex()) {
            when (val layerName = layer.name!!) {
                "Background" -> backgroundLayer = i
                "Floor" -> floorLayer = i
                "Foreground" -> foregroundLayer = i
                "Top" -> topLayer = i
                else -> println("Unknown layer: $layerName")
            }
        }
        backgroundLayer!!
        floorLayer!!
        foregroundLayer!!
        topLayer!!
        renderable = RenderableMultiplexer(listOf(
                ViewportResizerRenderable(tiledViewport),
                TiledMapRenderable(tiledMap, tiledCamera, intArrayOf(backgroundLayer, floorLayer)),
                StageRenderable(stage),
                TiledMapRenderable(tiledMap, tiledCamera, intArrayOf(foregroundLayer, topLayer)),
        ))
        resize(Gdx.graphics.width, Gdx.graphics.height)
    }

    fun teleport(x: Float, y: Float, movementId: UUID) {
        playerLocation.set(x, y)
        this.movementId = movementId
    }

    override fun update(delta: Float) {
        tempVector.setZero()
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            tempVector.y = 1f
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tempVector.y = -1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tempVector.x = 1f
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tempVector.x = -1f
        }
//            println(ArrayList<String>().apply { tiledMap.properties.keys.forEach { add(it) } })
        if (!tempVector.isZero) {
            tempVector.nor().scl(delta * 10)
        }
        playerLocation.add(tempVector)
        tiledViewport.camera.position.set(playerLocation.x * tileSize, playerLocation.y * tileSize, 0f)
        stageViewport.camera.position.set(playerLocation.x, playerLocation.y, 0f)
    }

    override fun render(delta: Float) {
        renderable.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        renderable.resize(width, height)
    }

    override fun dispose() {
        renderable.dispose()
    }
}
