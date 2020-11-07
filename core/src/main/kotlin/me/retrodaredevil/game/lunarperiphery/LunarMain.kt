package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import me.retrodaredevil.game.lunarperiphery.render.RenderObject
import me.retrodaredevil.game.lunarperiphery.screens.TitleScreen


class LunarMain : Game() {

    private lateinit var renderObject: RenderObject

    override fun create() {
        val batch: Batch = SpriteBatch()
        val mainSkin = Skin(Gdx.files.internal("skins/main/skin.json"))
        val uiSkin = Skin(Gdx.files.internal("skins/star-soldier/star-soldier-ui.json"))
        renderObject = RenderObject(batch, mainSkin, uiSkin)

        setScreen(TitleScreen(::changeScreen, renderObject))
    }
    private fun changeScreen(newScreen: Screen) {
        setScreen(newScreen)
    }

    override fun dispose() {
        super.dispose()
        renderObject.dispose()
    }

}