package com.phr.pong

import com.phr.components.SpriteRenderer
import com.phr.core.GameObject
import com.phr.scenes.Scene
import com.phr.core.Transform
import com.phr.io.KeyListener
import com.phr.renderer.Camera
import org.joml.Vector2f
import org.joml.Vector4f
import org.lwjgl.glfw.GLFW

class PongScene : Scene() {

    lateinit var playerPaddle : GameObject;
    lateinit var enemyPaddle : GameObject;
    lateinit var ball : GameObject;

    var gameStarted = false;

    var ballDirectionX = 0f
    var ballDirectionY = 0f

    private val xBounds = 1300f;
    private val yBounds = 650f;

    private lateinit var enemyPaddleAi : EnemyPaddleAi;

    override fun init() {
    }

    private fun startGame() {
        this.camera = Camera(Vector2f(0f, 0f));

        buildPlayerPaddle();
        buildPongBall();
        buildEnemyPaddle();

        enemyPaddleAi = EnemyPaddleAi(enemyPaddle, ball, 3f);

        addGameObjectToScene(playerPaddle);
        addGameObjectToScene(ball);
        addGameObjectToScene(enemyPaddle);

        gameStarted = true;

    }

    private fun buildPlayerPaddle() {
        playerPaddle = GameObject("Player"
            , Transform(Vector2f(50f,300f),  Vector2f(20f, 100f)), -1);
        val objectSprite = SpriteRenderer();
        playerPaddle.addComponent(objectSprite);
        objectSprite.color = Vector4f(0f, 0f, 0f, 1f);
    }

    private fun buildPongBall() {

        var initialBallPosition : Vector2f = playerPaddle.transform.position.clone() as Vector2f;
        initialBallPosition.add(Vector2f(playerPaddle.transform.scale.x + 1f, playerPaddle.transform.scale.y/2));

        ball = GameObject("Ball", Transform(initialBallPosition,  Vector2f(5f, 5f)), -1);

        val objectSprite = SpriteRenderer();
        ball.addComponent(objectSprite);
        objectSprite.color = Vector4f(1f, 0f, 0f, 1f);
    }

    private fun buildEnemyPaddle() {
        enemyPaddle = GameObject("EnemyPaddle"
            , Transform(Vector2f(1200f,300f),  Vector2f(20f, 100f)), -1);
        val objectSprite = SpriteRenderer();
        enemyPaddle.addComponent(objectSprite);
        objectSprite.color = Vector4f(0f, 0f, 0f, 1f);
    }

    override fun update(deltaTime: Float) {

        if (!gameStarted && KeyListener.keyPressed.get(GLFW.GLFW_KEY_ENTER)) {
            startGame();
        } else if (!gameStarted) {
            return;
        }

        enemyPaddleAi.decideNextMovement();

        checkCollisionBallAgainstScreen();
        checkCollisionBallAgainstPaddles();
        processPlayerInput();

        if (ballDirectionX != 0f || ballDirectionY != 0f) {
            ball.transform.position.add(ballDirectionX,ballDirectionY);
        }

        super.update(deltaTime);
    }

    private fun processPlayerInput() {
        if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_W)) {
            playerPaddle.transform.position.add(Vector2f(0f,3f));
        } else if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_S)) {
            playerPaddle.transform.position.add(Vector2f(0f,-3f));
        } else if (KeyListener.keyPressed.get(GLFW.GLFW_KEY_SPACE) && ballDirectionX == 0f && ballDirectionY == 0f) {
            ballDirectionX+= 6f;
            ballDirectionY+= 2f;
        }
    }

    private fun checkCollisionBallAgainstScreen() {

        val xBoundsBall = ball.transform.position.x + ball.transform.scale.x;
        val yBoundsBall = ball.transform.position.y + ball.transform.scale.y;

        if (xBoundsBall >= xBounds || xBoundsBall <= 0) {
            ballDirectionX *= -1;
        }

        if (yBoundsBall >= yBounds || yBoundsBall <= 0) {
            ballDirectionY *= -1;
        }
    }

    private fun checkCollisionBallAgainstPaddles() {

        if (objectsCollided(ball, playerPaddle)) {
            ballDirectionX *= -1;
        } else if (objectsCollided(ball, enemyPaddle)) {
            ballDirectionX *= -1;
        }

    }

    private fun objectsCollided(gameObject1 : GameObject, gameObject2 : GameObject) : Boolean {

        val transformObject1 = gameObject1.transform;
        val transformObject2 = gameObject2.transform;

        val collisionX = (transformObject1.position.x + transformObject1.scale.x >= transformObject2.position.x
                && transformObject2.position.x + transformObject2.scale.x >= transformObject1.position.x)

        val collisionY = (transformObject1.position.y + transformObject1.scale.y >= transformObject2.position.y
                && transformObject2.position.y + transformObject2.scale.y >= transformObject1.position.y)

        return collisionX && collisionY;
    }

}