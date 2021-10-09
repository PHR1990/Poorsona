package com.phr.core

import com.phr.components.Sprite
import com.phr.components.SpriteRenderer
import com.phr.components.Spritesheet
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import com.sun.management.GarbageCollectionNotificationInfo
import imgui.ImGui
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    lateinit var gameObject1 : GameObject ;
    lateinit var spritesheet : Spritesheet;

    override fun init() {

        loadResources();

        this.camera = Camera(Vector2f(-250f, 0f));

        spritesheet = AssetPool.getSpritesheet("assets/images/spritesheet.png")

        gameObject1 = GameObject("GO1", Transform(Vector2f(200f,100f),  Vector2f(256f, 256f)), -1);
        /*gameObject1.addComponent(SpriteRenderer(
            //spritesheet.sprites.get(0))
            Sprite(AssetPool.getTexture("assets/images/blendImage1.png")))
        )*/
        gameObject1.addComponent(SpriteRenderer(Vector4f(1f,0f,0f,1f))
        )

        addGameObjectToScene(gameObject1);

        this.activeGameObject = gameObject1;

        val gameObject2 = GameObject("GO1", Transform(Vector2f(400f,100f),  Vector2f(256f, 256f)), 2);
        gameObject2.addComponent(SpriteRenderer(
            //spritesheet.sprites.get(10))
            Sprite(AssetPool.getTexture("assets/images/blendImage2.png")))
        )
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

    override fun imGui() {
        ImGui.begin("Test window");
        ImGui.text("Some text");
        ImGui.end();
    }

}