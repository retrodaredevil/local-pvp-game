package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import me.retrodaredevil.game.lunarperiphery.render.*
import java.util.*

class GameMap(
        private val tiledMap: TiledMap
) : Updatable, Renderable {
    val tileSize = tiledMap.properties["tilewidth"] as Int
    val mapWidth = tiledMap.properties["width"] as Int
    val mapHeight = tiledMap.properties["height"] as Int
    private val tiledViewport: Viewport
    private val stageViewport: Viewport
    private val stage: Stage

    private val renderable: Renderable

    val world = World(Vector2.Zero, false)
    val playerBody = world.createBody(BodyDef().apply {
        type = BodyDef.BodyType.DynamicBody
    }).apply {
        createFixture(FixtureDef().apply {
            shape = PolygonShape().apply {
                setAsBox(0.5f, 0.5f)
            }
        })
    }
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
                WorldDebugRenderable(world, stageViewport.camera),
        ))
        resize(Gdx.graphics.width, Gdx.graphics.height)

        val foreground = tiledMap.layers[foregroundLayer] as TiledMapTileLayer
        for(x in 0 until mapWidth) for (y in 0 until mapHeight) {
            val cell = foreground.getCell(x, y)
            if (cell != null) {
                world.createBody(BodyDef().apply {
                    type = BodyDef.BodyType.StaticBody
                    position.set(x.toFloat(), y.toFloat())
                }).apply {
                    createFixture(FixtureDef().apply {
                        shape = PolygonShape().apply {
                            setAsBox(0.5f, 0.5f)
                        }
                    })
                }
            }
        }
    }

    fun teleport(x: Float, y: Float, movementId: UUID) {
        playerBody.setTransform(x, y, 0f)
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
        if (!tempVector.isZero) {
            tempVector.nor().scl(10.0f)
        }
        playerBody.linearVelocity = tempVector
        world.step(delta, 6, 2)
        tiledViewport.camera.position.set((playerBody.position.x + .5f) * tileSize, (playerBody.position.y + .5f) * tileSize, 0f)
        stageViewport.camera.position.set(playerBody.position.x, playerBody.position.y, 0f)
        tiledViewport.camera.update()
        stageViewport.camera.update()
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
