package me.retrodaredevil.game.lunarperiphery.render


class RenderableMultiplexer(
        private val list: List<Renderable>
) : Renderable {

    override fun render(delta: Float) {
        for(renderable in list){
            renderable.render(delta)
        }
    }
    override fun resize(width: Int, height: Int) {
        for(renderable in list){
            renderable.resize(width, height)
        }
    }

    override fun dispose() {
        for(renderable in list){
            renderable.dispose()
        }
    }

}
