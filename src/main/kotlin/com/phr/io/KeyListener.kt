package com.phr.io

import org.lwjgl.glfw.GLFW

object KeyListener {

    val keyPressed: Array<Boolean> = Array(350, {i -> false})
            get;

    fun keyPressedCallback(window: Long, keyIndex: Int , scancode:Int, action: Int, mods: Int) {
        if (action == GLFW.GLFW_PRESS) {
            keyPressed[keyIndex] = true;
        } else if (action == GLFW.GLFW_RELEASE) {
            keyPressed[keyIndex] = false;
        }
    }


}