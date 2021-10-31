package com.phr.components

import com.phr.configs.Configurations.GRID_HEIGHT
import com.phr.configs.Configurations.GRID_WIDTH
import com.phr.core.GameObject
import com.phr.gui.Window
import com.phr.io.MouseListener
import org.joml.Vector2f
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT
import kotlin.math.roundToInt

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
            holdingObject!!.transform.position.x = MouseListener.getOrthoX()
            holdingObject!!.transform.position.y = MouseListener.getOrthoY()

            snapHoldingObjectPosition();

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
        super.update(deltaTime)
    }

    private fun snapHoldingObjectPosition() {
        val truncatedX : Int = ((holdingObject!!.transform.position.x.toInt() / GRID_WIDTH) * GRID_WIDTH);
        val truncatedY : Int = ((holdingObject!!.transform.position.y.toInt() / GRID_HEIGHT) * GRID_HEIGHT);
        holdingObject!!.transform.position.x = truncatedX.toFloat() -4; // TODO cant figure out why it is always missing the snap
        holdingObject!!.transform.position.y = truncatedY.toFloat() -18
    }
}