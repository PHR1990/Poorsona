package com.phr.io

import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

object MouseListener {

    var scrollX: Double = 0.0
        private set;
    var scrollY: Double = 0.0
        private set;

    var posX: Double = 0.0
        private set;
    var posY: Double = 0.0
        private set;

    var lastPosX: Double = 0.0
        private set;
    var lastPosY: Double = 0.0
        private set;

    val mouseButtonPressed: Array<Boolean> = arrayOf(false, false, false)

    var isDragging: Boolean = false
        private set;

    fun mousePositionCallback(window: Long, posX: Double, posY: Double) {
        lastPosX = MouseListener.posX;
        lastPosY = MouseListener.posY;

        MouseListener.posX = posX;
        MouseListener.posY = posY;

    }

    fun mouseButtonCallback(window: Long, buttonIndex: Int, action: Int, mods: Int) {
        if (buttonIndex > mouseButtonPressed.size) {
            return;
        }

        if (action == GLFW_PRESS) {
            mouseButtonPressed[buttonIndex] = true;
        } else if (action == GLFW_RELEASE) {
            mouseButtonPressed[buttonIndex] = false;
            isDragging = false;
        }

    }

    fun mouseScrollCallback(window: Long, scrollX: Double, scrollY : Double) {
        MouseListener.scrollX = scrollX;
        MouseListener.scrollY = scrollY;
    }
}