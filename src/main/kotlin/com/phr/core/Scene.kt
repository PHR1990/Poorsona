package com.phr.core

import com.phr.renderer.Camera
import com.phr.renderer.Renderer

abstract class Scene {

    var renderer = Renderer();
    lateinit var camera: Camera
        protected set

    var gameObjects : MutableList<GameObject> = ArrayList();
    var isRunning : Boolean = false;

    abstract fun update(deltaTime: Float) : Unit;

    open fun init() {
    }

    fun start() {
        gameObjects.forEach{
            it.start();
            this.renderer.add(it);
        }
        isRunning = true;
    }

    fun addGameObjectToScene(gameObject : GameObject) {
        gameObjects.add(gameObject);

        if (isRunning) {
            gameObject.start();
            renderer.add(gameObject)
        }
    }
}