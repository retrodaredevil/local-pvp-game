package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import me.retrodaredevil.game.lunarperiphery.render.*
import java.util.*
import kotlin.math.max

class GameMap(
        tiledMap: TiledMap,
        renderObject: RenderObject,
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
    val cameraPosition = Vector2()
    private val playerGroup: Group
    var movementId = UUID.randomUUID()
    private val tempVector = Vector2()

    init {
        val tiledCamera = OrthographicCamera()
        tiledViewport = ExtendViewport(tileSize * 20f, tileSize * 15f, tiledCamera)

        stageViewport = ExtendViewport(20f, 15f)
        stage = Stage(stageViewport)
        playerGroup = Group()
        stage.addActor(playerGroup)
        playerGroup.addActor(Image(renderObject.mainSkin.newDrawable("alien", Color.RED)).apply {
            setBounds(-.5f, -.5f, 1f, 2f)
        })

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
                TiledMapRenderable(tiledMap, tiledCamera, intArrayOf(backgroundLayer, floorLayer, foregroundLayer)),
                StageRenderable(stage),
                TiledMapRenderable(tiledMap, tiledCamera, intArrayOf(topLayer)),
                ToggleRenderable(WorldDebugRenderable(world, stageViewport.camera)) { Gdx.input.isKeyJustPressed(Input.Keys.H) },
        ))
        resize(Gdx.graphics.width, Gdx.graphics.height)

        val foreground = tiledMap.layers[foregroundLayer] as TiledMapTileLayer
        for(x in 0 until mapWidth) for (y in 0 until mapHeight) {
            val cell = foreground.getCell(x, y)
            if (cell != null) {
                val tileId = cell.tile.id // note that this is 1 based, unlike in the tiled editor, where the ID is 0 based. This means this is +1 from the expected value
                world.createBody(BodyDef().apply {
                    type = BodyDef.BodyType.StaticBody
                    position.set(x.toFloat(), y.toFloat())
                }).apply {
                    createFixture(FixtureDef().apply {
                        shape = when (tileId) {
                            in listOf(68) -> ShapeConstants.TRIANGLE_SMOOTH_LOWER_LEFT
                            in listOf(69) -> ShapeConstants.TRIANGLE_SMOOTH_LOWER_RIGHT
                            in listOf(38) -> ShapeConstants.TRIANGLE_SMOOTH_UPPER_LEFT
                            in listOf(39) -> ShapeConstants.TRIANGLE_SMOOTH_UPPER_RIGHT
                            else -> ShapeConstants.BOX_SMOOTH_EDGES
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
            tempVector.y += 1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tempVector.y += -1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tempVector.x += 1f
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tempVector.x += -1f
        }
        if (!tempVector.isZero) {
            tempVector.nor().scl(10.0f)
        }
        playerBody.linearVelocity = tempVector
        world.step(delta, 6, 2)
        val playerPosition = playerBody.position
        playerGroup.setPosition(playerPosition.x, playerPosition.y)
        val distance = playerPosition.dst(cameraPosition)
        if (distance > 5 || distance < 0.1f) {
            cameraPosition.set(playerPosition)
        } else {
            val direction = playerPosition.cpy().sub(cameraPosition).nor()
            val speed = max(distance * distance, 1.0f) * 7
            direction.scl(delta * speed)
            cameraPosition.add(direction)
        }
        val cameraPosition = cameraPosition
        tiledViewport.camera.position.set((cameraPosition.x + .5f) * tileSize, (cameraPosition.y + .5f) * tileSize, 0f)
        stageViewport.camera.position.set(cameraPosition.x, cameraPosition.y, 0f)
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
