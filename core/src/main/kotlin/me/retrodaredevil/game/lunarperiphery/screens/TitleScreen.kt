package me.retrodaredevil.game.lunarperiphery.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.viewport.FitViewport
import me.retrodaredevil.game.lunarperiphery.render.RenderObject

class TitleScreen(
        private val screenChanger: ScreenChanger,
        private val renderObject: RenderObject
) : ScreenAdapter() {

    val hostGameButton: Button
    val joinGameButton: Button

    val uiStage: Stage

    init {
        val style = renderObject.uiSkin.get(TextButton.TextButtonStyle::class.java)
        hostGameButton = TextButton("Host Game", style)
        joinGameButton = TextButton("Join Game", style)

        val table = Table()
        table.setFillParent(true)
        table.add(Label("Lunar Periphery", renderObject.uiSkin))
        table.row()
        table.add(hostGameButton).row()
        table.add(joinGameButton).row()

        uiStage = Stage(FitViewport(640f, 640f), renderObject.batch)
        uiStage.addActor(table)
        Gdx.input.inputProcessor = uiStage
    }

    override fun render(delta: Float) {
        uiStage.act(delta)

        if (hostGameButton.isPressed) {
            println("Hosting")
        }
        if (joinGameButton.isPressed) {

        }

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        uiStage.draw()
    }

    override fun dispose() {
        uiStage.dispose()
    }
}
