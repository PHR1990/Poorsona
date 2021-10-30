package com.phr.scenes

import com.google.gson.GsonBuilder
import com.phr.components.Component
import com.phr.components.ComponentDeserializer
import com.phr.core.GameObject
import com.phr.core.GameObjectDeserializer
import com.phr.renderer.Camera
import com.phr.renderer.Renderer
import imgui.ImGui
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

abstract class Scene {

    var renderer = Renderer();
    lateinit var camera: Camera
        protected set

    var gameObjects : MutableList<GameObject> = ArrayList();
    var isRunning : Boolean = false;
    var levelLoaded = false;
    protected var activeGameObject : GameObject? = null;

    open fun update(deltaTime: Float) {

        gameObjects.forEach { it.update(deltaTime) }

        renderer.render();
    }

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

    fun saveExit() {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component::class.java, ComponentDeserializer())
            .registerTypeAdapter(GameObject::class.java, GameObjectDeserializer())
            .create();

        val fileWriter = FileWriter("level.txt");
        fileWriter.write(gson.toJson(this.gameObjects));
        fileWriter.close();
    }

    fun load() {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Component::class.java, ComponentDeserializer())
            .registerTypeAdapter(GameObject::class.java, GameObjectDeserializer())
            .create();

        var infile : String = "";
        try {
            infile = String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (e : IOException) {
            return;
        }
        if (!infile.equals("")) {
            var maxGameObjectId = -1;
            var maxComponentId = -1;
            val gameObjects = gson.fromJson(infile, Array<GameObject>::class.java)

            gameObjects.forEach {
                gameObject: GameObject ->
                addGameObjectToScene(gameObject);


            }
            maxGameObjectId++;
            maxComponentId++;

            levelLoaded = true;
        }
    }
}