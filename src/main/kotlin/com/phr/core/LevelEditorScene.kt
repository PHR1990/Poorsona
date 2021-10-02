package com.phr.core

import com.phr.components.Sprite
import com.phr.components.SpriteRenderer
import com.phr.components.Spritesheet
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    override fun init() {

        loadResources();

        this.camera = Camera(Vector2f(-250f, 0f));

        val spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png")

        val gameObject1 = GameObject("GO1", Transform(Vector2f(100f,100f),  Vector2f(256f, 256f)));
        gameObject1.addComponent(SpriteRenderer(spritesheet.sprites.get(0)))

        addGameObjectToScene(gameObject1);

        val gameObject2 = GameObject("GO1", Transform(Vector2f(400f,100f),  Vector2f(256f, 256f)));
        gameObject2.addComponent(SpriteRenderer(spritesheet.sprites.get(10)))
        addGameObjectToScene(gameObject2);

    }

    fun loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet("assets/images/spritesheet.png",
            Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png")
                ,16, 16, 26 , 0));
    }

    override fun update(deltaTime: Float) {

        gameObjects.forEach { it.update(deltaTime) }

        renderer.render();

    }
}