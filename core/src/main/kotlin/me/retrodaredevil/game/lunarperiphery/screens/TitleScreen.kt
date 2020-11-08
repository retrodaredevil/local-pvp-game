package me.retrodaredevil.game.lunarperiphery.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import me.retrodaredevil.game.lunarperiphery.render.*

class TitleScreen(
        private val screenChanger: ScreenChanger,
        private val renderObject: RenderObject
) : ScreenAdapter() {

    val hostGameButton: Button
    val joinGameButton: Button
    val ipAddressField: TextField

    val uiStage: Stage

    val renderable: Renderable

    init {
        val textButtonStyle = renderObject.uiSkin.get(TextButton.TextButtonStyle::class.java).let {
            TextButton.TextButtonStyle(it).apply {
                over = down
            }
        }
        hostGameButton = TextButton("Host Game", textButtonStyle)
        joinGameButton = TextButton("Join Game", textButtonStyle)
        val textFieldStyle = renderObject.uiSkin.get(TextField.TextFieldStyle::class.java)
        ipAddressField = TextField("192.168.1.10", textFieldStyle)
        ipAddressField.alignment = Align.center

        val table = Table()
        table.setFillParent(true)
        table.add(Label("Lunar Periphery", renderObject.uiSkin.get("title", Label.LabelStyle::class.java)))
        table.row()
        table.add(hostGameButton).width(400f).row()
        table.add(joinGameButton).width(400f).row()
        table.add(ipAddressField).width(400f).row()


        uiStage = Stage(FitViewport(900f, 800f), renderObject.batch)
        uiStage.addActor(table)
        Gdx.input.inputProcessor = uiStage

        renderable = RenderableMultiplexer(listOf(ResetRenderable(Color.BLACK), StageRenderable(uiStage)))
    }

    override fun resize(width: Int, height: Int) {
        renderable.resize(width, height)
    }

    override fun render(delta: Float) {
        uiStage.act(delta)

        if (hostGameButton.isPressed) {
            println("Hosting")
        }
        if (joinGameButton.isPressed) {

        }

        renderable.render(delta)
    }

    override fun dispose() {
        uiStage.dispose()
    }
}
