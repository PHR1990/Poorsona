package com.phr.pong

import com.phr.core.GameObject
import org.joml.Vector2f
import kotlin.random.Random

class EnemyPaddleAi(val enemyPaddleGameObject: GameObject, val ballGameObject: GameObject,
    val movementSpeed : Float) {

    var ballPreviousPosition = Vector2f();

    fun decideNextMovement() {
        // Dice roll to do a proper decision
        val decision = Random.nextInt(-1, 11);

        val middlePointPaddle = enemyPaddleGameObject.transform.scale.y/2;

        val currentBallMovement = ballGameObject.transform.position.clone() as Vector2f;
        currentBallMovement.sub(ballPreviousPosition)

        if (decision <= -1) {
            // Do nothing
        } else {
            if (ballGameObject.transform.position.x < 500) {
                return;
            }

            if (ballGameObject.transform.position.y
                > (enemyPaddleGameObject.transform.position.y + middlePointPaddle)) {
                moveUp()
            } else if (ballGameObject.transform.position.y
                < (enemyPaddleGameObject.transform.position.y - middlePointPaddle)) {
                moveDown()
            } else {
                if (currentBallMovement.y > 0) {
                    moveUp()
                } else {
                    moveDown()
                }
            }
        }

        ballPreviousPosition = ballGameObject.transform.position;
    }

    private fun moveUp() {
        enemyPaddleGameObject.transform.position.y+= movementSpeed;
    }

    private fun moveDown() {
        enemyPaddleGameObject.transform.position.y-= movementSpeed;
    }

}