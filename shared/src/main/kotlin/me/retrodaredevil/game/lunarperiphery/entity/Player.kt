package me.retrodaredevil.game.lunarperiphery.entity

import com.badlogic.gdx.math.Vector2
import java.util.*

class Player(
        override val id: UUID,
        val isLocal: Boolean
) : Entity {
    private val playerLocation = Vector2()
    var movementId = UUID.randomUUID()
    override var location: Vector2
        get() = playerLocation.cpy()
        set(value) {
            playerLocation.set(value)
        }

    override fun getLocation(vector2: Vector2) {
        vector2.set(playerLocation)
    }

    override fun setLocation(x: Float, y: Float) {
        playerLocation.set(x, y)
    }

    override val x: Float
        get() = playerLocation.x
    override val y: Float
        get() = playerLocation.y
}
