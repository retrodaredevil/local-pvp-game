package me.retrodaredevil.game.lunarperiphery.entity

import com.badlogic.gdx.math.Vector2
import java.util.*

interface Entity {
    val uuid: UUID

    val location: Vector2
    fun getLocation(vector2: Vector2)
    val x: Float
    val y: Float

}
