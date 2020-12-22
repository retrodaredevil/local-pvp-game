package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import me.retrodaredevil.game.lunarperiphery.render.RenderObject
import me.retrodaredevil.game.lunarperiphery.screens.TitleScreen
import kotlin.math.min


class LunarMain : Game() {

    private lateinit var renderObject: RenderObject

    private var nextScreen: Screen? = null

    override fun create() {
        val batch: Batch = SpriteBatch()
        val mainSkin = Skin(Gdx.files.internal("skins/main/skin.json"))
        val uiSkin = Skin(Gdx.files.internal("skins/star-soldier/star-soldier-ui.json"))
        renderObject = RenderObject(batch, mainSkin, uiSkin)

        setScreen(TitleScreen(::changeScreen, renderObject))
    }
    private fun changeScreen(newScreen: Screen) {
        nextScreen = newScreen
    }

    override fun render() {
        nextScreen?.let {
            setScreen(it)
            nextScreen = null
        }
        val delta = Gdx.graphics.deltaTime
        if (delta > 1 / 20f) {
            println("delta: $delta")
        }
        screen?.render(min(delta, 1 / 20f))

        Gdx.graphics.setTitle("Lunar Periphery - FPS: ${Gdx.graphics.framesPerSecond}")
    }

    override fun dispose() {
        super.dispose()
        renderObject.dispose()
    }

}
