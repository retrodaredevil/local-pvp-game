@file:JvmName("Lwjgl3Launcher")
package me.retrodaredevil.game.lunarperiphery.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import me.retrodaredevil.game.lunarperiphery.LunarMain

fun main(args: Array<String>) {
    createApplication()
}

private fun createApplication(): Lwjgl3Application {
    val configuration = Lwjgl3ApplicationConfiguration()
    configuration.setTitle("lunar-periphery")
    configuration.setWindowedMode(640, 480)
    configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png")

    return Lwjgl3Application(LunarMain(), configuration)
}