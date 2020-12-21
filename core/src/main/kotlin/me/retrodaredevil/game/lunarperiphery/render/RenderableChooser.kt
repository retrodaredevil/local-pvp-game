package me.retrodaredevil.game.lunarperiphery.render

class RenderableChooser : Renderable {
    var renderable: Renderable? = null
        set(value) {
            field?.dispose()
            field = value
        }

    override fun render(delta: Float) {
        renderable?.render(delta)
    }

    override fun resize(width: Int, height: Int) {
        renderable?.resize(width, height)
    }

    override fun dispose() {
        renderable?.dispose()
    }
}
