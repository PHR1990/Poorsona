package com.phr.components

import com.phr.core.GameObject
import com.phr.gui.Window
import com.phr.io.MouseListener
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT

class MouseControls : Component() {

    var holdingObject : GameObject? = null;

    fun pickupObject(gameObject : GameObject) {
        holdingObject = gameObject;
        Window.currentScene.addGameObjectToScene(gameObject)
    }

    fun place() {
        holdingObject = null;
    }

    override fun update(deltaTime: Float) {
        if (holdingObject != null) {
            holdingObject!!.transform.position.x = MouseListener.getOrthoX() - 16
            holdingObject!!.transform.position.y = MouseListener.getOrthoY() - 16

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
        super.update(deltaTime)
    }

}