package me.retrodaredevil.game.lunarperiphery.entity

import com.badlogic.gdx.math.Vector2
import java.util.*

interface Entity {
    val id: UUID

    var location: Vector2
    fun getLocation(vector2: Vector2)
    fun setLocation(x: Float, y: Float)
    val x: Float
    val y: Float

}
