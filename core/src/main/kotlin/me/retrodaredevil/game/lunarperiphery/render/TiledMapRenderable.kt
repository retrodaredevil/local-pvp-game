package me.retrodaredevil.game.lunarperiphery.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class TiledMapRenderable(
        tiledMap: TiledMap,
        private val cameraToFollow: OrthographicCamera
) : Renderable {

    private val tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    override fun render(delta: Float) {
        tiledMapRenderer.setView(cameraToFollow)
        cameraToFollow.update()
        tiledMapRenderer.render()
        println("Rendering at ${cameraToFollow.position}")
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        tiledMapRenderer.dispose()
    }
}
