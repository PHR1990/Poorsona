package com.phr.components

import com.phr.configs.Configurations.GRID_HEIGHT
import com.phr.configs.Configurations.GRID_WIDTH
import com.phr.gui.Window
import com.phr.renderer.DebugDraw.addLine2D
import org.joml.Vector2f
import org.joml.Vector3f
import kotlin.math.max
import kotlin.math.roundToInt

class GridLines : Component() {

    override fun update(deltaTime: Float) {
        var cameraPos = Window.currentScene.camera.position;
        var projectionSize = Window.currentScene.camera.projectionSize;

        val firstX = (((cameraPos.x/ GRID_WIDTH) - 1) * GRID_WIDTH).roundToInt();
        val firstY = (((cameraPos.y/ GRID_HEIGHT) - 1) * GRID_HEIGHT).roundToInt();

        val numberVerticalLines = (projectionSize.x / GRID_WIDTH).roundToInt() + 2;
        val numberHorizontalLines = (projectionSize.y / GRID_HEIGHT).roundToInt() + 2;

        val height = (projectionSize.y + GRID_HEIGHT * 2).roundToInt()
        val width = (projectionSize.x + GRID_WIDTH * 2).roundToInt();

        val maxLines = max(numberVerticalLines, numberHorizontalLines)
        val color = Vector3f(0.2f, 0.2f, 0.2f);

        for (i in 0 until maxLines) {
            val x = firstX + (GRID_WIDTH * i)
            val y = firstY + (GRID_HEIGHT * i)

            if (i < numberVerticalLines) {
                addLine2D(Vector2f(x.toFloat(), firstY.toFloat()), Vector2f(x.toFloat(), y.toFloat() + height), color)
            }

            if (i < numberHorizontalLines) {
                addLine2D(Vector2f(firstX.toFloat(), y.toFloat()), Vector2f(firstX.toFloat() + width, y.toFloat()), color)
            }
        }
    }

}