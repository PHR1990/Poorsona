package com.phr.core

import com.phr.components.SpriteRenderer
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    override fun init() {
        this.camera = Camera(Vector2f(-250f, 0f));

        val gameObject1 = GameObject("GO1", Transform(Vector2f(100f,100f),  Vector2f(256f, 256f)));
        gameObject1.addComponent(SpriteRenderer(Vector4f(1f, 1f, 1f,1f),
            AssetPool.getTexture("assets/images/testImage.png")))
        addGameObjectToScene(gameObject1);

        val gameObject2 = GameObject("GO1", Transform(Vector2f(400f,100f),  Vector2f(256f, 256f)));
        gameObject2.addComponent(SpriteRenderer(Vector4f(1f, 1f, 1f,1f),
            AssetPool.getTexture("assets/images/testImage2.png")))
        addGameObjectToScene(gameObject2);

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