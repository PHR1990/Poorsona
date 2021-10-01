package com.phr.core

import com.phr.components.SpriteRenderer
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    override fun init() {
        this.camera = Camera(Vector2f(-250f, 0f));

        val xOffset = 10;
        val yOffset = 10;

        val totalWidth = 600 - (xOffset * 2);
        val totalHeight = 300 - (yOffset * 2);

        val sizeX = totalWidth / 100f;
        val sizeY = totalHeight / 100f;

        val padding = 0;

        for (x in 0 until 100) {
            for (y in 0 until 100) {
                val xPos = xOffset + (x * sizeX) + (padding * x);
                val yPos = yOffset + (y * sizeY) + (padding * y);

                var gameObject = GameObject("obj", Transform(Vector2f(xPos, yPos), Vector2f(sizeX, sizeY)));
                gameObject.addComponent(SpriteRenderer(
                    Vector4f(xPos / totalWidth, yPos/totalHeight, 1f, 1f))
                )
                addGameObjectToScene(gameObject);
            }

        }

        loadResources();
    }

    fun loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
    }

    override fun update(deltaTime: Float) {

        gameObjects.forEach { it.update(deltaTime) }

        renderer.render();

    }
}