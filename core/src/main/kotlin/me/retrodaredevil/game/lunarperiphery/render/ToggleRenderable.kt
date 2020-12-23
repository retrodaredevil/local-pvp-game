package me.retrodaredevil.game.lunarperiphery.render

class ToggleRenderable(
        private val renderable: Renderable,
        private val toggle: () -> Boolean
) : Renderable {
    private var show = true
    override fun render(delta: Float) {
        if (toggle()) {
            show = !show;
        }
        if (show) {
            renderable.render(delta)
        }
    }

    override fun resize(width: Int, height: Int) {
        renderable.resize(width, height)
    }

    override fun dispose() {
        renderable.dispose()
    }
}
