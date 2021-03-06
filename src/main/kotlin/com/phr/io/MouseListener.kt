package com.phr.io

import com.phr.gui.Window
import org.joml.Matrix4f
import org.joml.Vector4f
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

    fun getOrthoX() : Float {

        var currentX = posX
        currentX = (currentX / Window.width) * 2f - 1f;

        val temp = Vector4f(currentX.toFloat(), 0f, 0f, 1f);
        val tempInverseProjection : Matrix4f = Window.currentScene.camera.inverseProjection.clone() as Matrix4f;
        val tempInverseView : Matrix4f = Window.currentScene.camera.inverseView.clone() as Matrix4f;

        temp.mul(tempInverseProjection).mul(tempInverseView);

        return temp.x;

    }

    fun getOrthoY() : Float {

        var currentY = Window.height - posY;
        currentY = (currentY /Window.height) * 2f - 1f;

        val temp = Vector4f(0f, currentY.toFloat(), 0f, 1f);
        val tempInverseProjection : Matrix4f = Window.currentScene.camera.inverseProjection.clone() as Matrix4f;
        val tempInverseView : Matrix4f = Window.currentScene.camera.inverseView.clone() as Matrix4f;

        temp.mul(tempInverseProjection).mul(tempInverseView);

        return temp.y;
    }

    fun mouseScrollCallback(window: Long, scrollX: Double, scrollY : Double) {
        MouseListener.scrollX = scrollX;
        MouseListener.scrollY = scrollY;
    }

    fun mouseButtonDown(button : Int) : Boolean {
        if (button < mouseButtonPressed.size) {
            return mouseButtonPressed[button]
        }

        return false;

    }
}