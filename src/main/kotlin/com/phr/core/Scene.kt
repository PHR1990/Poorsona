package com.phr.core

import com.phr.renderer.Camera
import com.phr.renderer.Renderer
import imgui.ImGui

abstract class Scene {

    var renderer = Renderer();
    lateinit var camera: Camera
        protected set

    var gameObjects : MutableList<GameObject> = ArrayList();
    var isRunning : Boolean = false;
    protected var activeGameObject : GameObject? = null;

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

    fun sceneImGui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject!!.imGui();
            ImGui.end();

        }

        imGui();
    }

    open fun imGui() {

    }
}