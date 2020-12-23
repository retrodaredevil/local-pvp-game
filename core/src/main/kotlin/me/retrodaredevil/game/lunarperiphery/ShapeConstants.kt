package me.retrodaredevil.game.lunarperiphery

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.PolygonShape

object ShapeConstants {
    val TRIANGLE_SMOOTH_LOWER_LEFT = PolygonShape().apply {
        set(floatArrayOf(
                -.5f, .5f,
                .5f, .5f,
                .5f, -.5f,
        ))
    }
    val TRIANGLE_SMOOTH_LOWER_RIGHT = PolygonShape().apply {
        set(floatArrayOf(
                -.5f, -.5f,
                -.5f, .5f,
                .5f, .5f,
        ))
    }
    val TRIANGLE_SMOOTH_UPPER_LEFT = PolygonShape().apply {
        set(arrayOf(
                Vector2(-.5f, -.5f),
                Vector2(.5f, .5f),
                Vector2(.5f, -.5f),
        ))
    }
    val TRIANGLE_SMOOTH_UPPER_RIGHT = PolygonShape().apply {
        set(arrayOf(
                Vector2(-.5f, .5f),
                Vector2(-.5f, -.5f),
                Vector2(.5f, -.5f),
        ))
    }
    val BOX_SMOOTH_EDGES = ChainShape().apply {
        createLoop(floatArrayOf(
                -.4f, -.5f,
                -.45f, -.45f,
                -.5f, -.4f,

                -.5f, .4f,
                -.45f, .45f,
                -.4f, .5f,

                .4f, .5f,
                .45f, .45f,
                .5f, .4f,

                .5f, -.4f,
                .45f, -.45f,
                .4f, -.5f
        ))
    }
}
