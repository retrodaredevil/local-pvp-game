package me.retrodaredevil.game.lunarperiphery.render

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class TiledMapRenderable(
        tiledMap: TiledMap,
        private val cameraToFollow: OrthographicCamera,
        private val layersToRender: IntArray? = null
) : Renderable {

    private val tiledMapRenderer = OrthogonalTiledMapRenderer(tiledMap)

    override fun render(delta: Float) {
        tiledMapRenderer.setView(cameraToFollow)
        cameraToFollow.update() // the camera's position has likely been updated elsewhere, so let's make sure the other internal stuff is up to date
        if (layersToRender == null) {
            tiledMapRenderer.render()
        } else {
            tiledMapRenderer.render(layersToRender)
        }
//        println("Rendering at ${cameraToFollow.position}")
    }

    override fun resize(width: Int, height: Int) {
    }

    override fun dispose() {
        tiledMapRenderer.dispose()
    }
}
