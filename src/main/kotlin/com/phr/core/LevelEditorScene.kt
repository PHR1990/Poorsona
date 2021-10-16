package com.phr.core

import com.phr.components.RigidBody
import com.phr.components.SpriteRenderer
import com.phr.components.Spritesheet
import com.phr.configs.Configurations.SPRITE_SHEETS_FOLDER
import com.phr.io.MouseListener
import com.phr.renderer.Camera
import com.phr.util.AssetPool
import imgui.ImGui
import imgui.ImVec2
import org.joml.Vector2f
import org.joml.Vector4f

class LevelEditorScene : Scene() {

    lateinit var gameObject1 : GameObject;
    lateinit var spritesheet : Spritesheet;

    private val DECORATIONS_AND_BLOCKS_SPRITESHEETS_PATH = SPRITE_SHEETS_FOLDER + "decorationsAndBlocks.png"

    override fun init() {

        loadResources();

        spritesheet = AssetPool.getSpritesheet(DECORATIONS_AND_BLOCKS_SPRITESHEETS_PATH)

        this.camera = Camera(Vector2f(-100f, -50f));

        if (levelLoaded) {
            this.activeGameObject = gameObjects.get(0)

            return;
        }

        gameObject1 = GameObject("GO1", Transform(Vector2f(200f,100f),  Vector2f(256f, 256f)), -1);
        val objectSprite = SpriteRenderer();
        gameObject1.addComponent(objectSprite);
        objectSprite.color = Vector4f(1f, 0f, 0f, 1f);

        //gameObject1.addComponent(SpriteRenderer(Vector4f(1f,0f,0f,1f)))
        //gameObject1.addComponent(RigidBody())

        addGameObjectToScene(gameObject1);

        this.activeGameObject = gameObject1;

        /*val gameObject2 = GameObject("GO1", Transform(Vector2f(400f,100f),  Vector2f(256f, 256f)), 2);
        gameObject2.addComponent(SpriteRenderer(
            //spritesheet.sprites.get(10))
            Sprite(AssetPool.getTexture("assets/images/blendImage2.png")))
        )
        addGameObjectToScene(gameObject2);
        */

    }

    fun loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.addSpritesheet(
            DECORATIONS_AND_BLOCKS_SPRITESHEETS_PATH,
            Spritesheet(AssetPool.getTexture(DECORATIONS_AND_BLOCKS_SPRITESHEETS_PATH,)
                ,16, 16, 81 , 0));
    }

    override fun update(deltaTime: Float) {

        gameObjects.forEach { it.update(deltaTime) }

        print(MouseListener.getOrthoX());
        print("  ");
        println(MouseListener.getOrthoY());

        renderer.render();

    }

    override fun imGui() {
        ImGui.begin("Test window");

        val windowPos = ImVec2();
        ImGui.getWindowPos(windowPos);
        val windowSize = ImVec2();
        ImGui.getWindowSize(windowSize);
        val itemSpacing = ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        val windowX2 = windowPos.x + windowSize.x;

        var buttonId = 0;
        spritesheet.sprites.forEach{
            val spriteWidth = it.width;
            val spriteHeight = it.height;
            val id = it.texture?.textureId ?: -1;

            val textureCoordinates = it.textureCoordinates;

            ImGui.pushID(buttonId);
            buttonId++;
            if (ImGui.imageButton(id, spriteWidth.toFloat(), spriteHeight.toFloat(), textureCoordinates[0].x, textureCoordinates[0].y
                    , textureCoordinates[2].x, textureCoordinates[2].y)) {
                println("Button " + id + "clicked");
            }
            ImGui.popID();

                val lastButtonPos = ImVec2();
                ImGui.getItemRectMax(lastButtonPos);
                val lastButtonX2 = lastButtonPos.x;
                val nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                if (nextButtonX2 < windowX2) {
                    ImGui.sameLine();
                }

        };

        ImGui.end();
    }

}