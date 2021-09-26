package com.phr.core

import com.phr.renderer.Camera

abstract class Scene {

    protected lateinit var camera: Camera;
    var gameObjects : MutableList<GameObject> = ArrayList();
    var isRunning : Boolean = false;


    abstract fun update(deltaTime: Float) : Unit;

    open fun init() {

    }

    fun start() {
        gameObjects.forEach{
            it.start();
        }
        isRunning = true;
    }

    fun addGameObjectToScene(gameObject : GameObject) {
        gameObjects.add(gameObject);

        if (isRunning) {
            gameObject.start();
        }
    }
}