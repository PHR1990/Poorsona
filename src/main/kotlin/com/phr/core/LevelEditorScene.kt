package com.phr.core

import com.phr.components.Sprite
import com.phr.components.SpriteRenderer
import com.phr.components.Spritesheet
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import com.sun.management.GarbageCollectionNotificationInfo
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    lateinit var gameObject1 : GameObject ;
    lateinit var spritesheet : Spritesheet;

    var spriteIndex = 0;
    var spriteFlipTime = 0.2f;
    var spriteFlipTimeLeft = 0f

    override fun init() {

        loadResources();

        this.camera = Camera(Vector2f(-250f, 0f));

        spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png")

        gameObject1 = GameObject("GO1", Transform(Vector2f(100f,100f),  Vector2f(256f, 256f)));
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
        spriteFlipTimeLeft-= deltaTime;
        if (spriteFlipTimeLeft <= 0) {
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex > 4) {
                spriteIndex=0;
            }
            gameObject1.getComponent(SpriteRenderer::class.java)!!.sprite = spritesheet.sprites.get(spriteIndex);
        }
        gameObjects.forEach { it.update(deltaTime) }

        renderer.render();

    }
}