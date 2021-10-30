package com.phr.pong

import com.phr.core.GameObject
import kotlin.random.Random

class EnemyPaddleAi(val enemyPaddleGameObject: GameObject, val ballGameObject: GameObject,
    val movementSpeed : Float) {

    fun decideNextMovement() {
        val decision = Random.nextInt(-1, 11);

        if (decision <= 2) {
            // Do nothing
        } else { // Dice roll to do a proper decision
            if (ballGameObject.transform.position.y > enemyPaddleGameObject.transform.position.y) {
                moveUp()
            } else {
                moveDown()
            }
        }

    }

    private fun moveUp() {
        enemyPaddleGameObject.transform.position.y+= movementSpeed;
    }

    private fun moveDown() {
        enemyPaddleGameObject.transform.position.y-= movementSpeed;
    }

}