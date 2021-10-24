package com.phr.core

import com.phr.components.SpriteRenderer
import com.phr.io.KeyListener
import com.phr.io.MouseListener
import com.phr.renderer.Camera
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFWKeyCallback

class PongScene : Scene() {

    lateinit var playerPaddle : GameObject;
    lateinit var enemyPaddle : GameObject;
    lateinit var ball : GameObject;


    override fun init() {

        this.camera = Camera(Vector2f(0f, 0f));

        playerPaddle = GameObject("Player", Transform(Vector2f(50f,300f),  Vector2f(20f, 100f)), -1);
        val objectSprite = SpriteRenderer();
        playerPaddle.addComponent(objectSprite);
        objectSprite.color = Vector4f(0f, 0f, 0f, 1f);

        addGameObjectToScene(playerPaddle);

        this.activeGameObject = playerPaddle;
        //addGameObjectToScene(enemyPaddle);
        //addGameObjectToScene(ball);
    }

    override fun update(deltaTime: Float) {

        if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_W)) {
            playerPaddle.transform.position.add(Vector2f(0f,3f));
        } else if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_S)) {
            playerPaddle.transform.position.add(Vector2f(0f,-3f));
        } else if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_SPACE)) {
            // Start ball
        }

        super.update(deltaTime);

    }


}