package com.phr.gui

import com.phr.core.LevelEditorScene
import com.phr.core.LevelScene
import com.phr.core.Scene
import com.phr.io.KeyListener
import com.phr.io.MouseListener
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

object Window {
    private var windowReference: Long = 0;

    private val width = 1920;
    private val height = 1080;

    private val title = "com.phr.gui.Window test";

    lateinit var currentScene: Scene
        private set

    var red = 0f
    var green = 0f
    var blue = 0f
    var alpha = 0f

    private fun changeScene(newSceneIndex : Int) : Unit {
        currentScene = when (newSceneIndex) {
            0 -> LevelEditorScene();
            1 -> LevelScene();
            else -> {
                throw Error("Unknown scene");
            }
        }
        currentScene.init();
        currentScene.start();

    }

    fun run() {
        initializeOpenGlWindow()

        initializeListeners()

        changeScene(0);
        gameLoop()

        dispose()

    }

    fun initializeOpenGlWindow() {

        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW

        check(glfwInit()) { "Unable to initialize GLFW." }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        windowReference = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        check(windowReference != MemoryUtil.NULL) { "Failed to create the GLFW window." }

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowReference);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(windowReference);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    private fun initializeListeners() {
        glfwSetCursorPosCallback(windowReference, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(windowReference, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowReference, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(windowReference, KeyListener::keyPressedCallback)

    }

    fun gameLoop() {

        var beginTime: Float = glfwGetTime().toFloat();
        var deltaTime: Float = -1f;
        while (!glfwWindowShouldClose(windowReference)) {



            glfwPollEvents();

            GL11.glClearColor(red, green, blue, alpha);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0) {
                currentScene.update(deltaTime);
                println("FPS:" + (1.0f)/deltaTime);
            }

            glfwSwapBuffers(windowReference)

            deltaTime = glfwGetTime().toFloat() - beginTime;

            beginTime = glfwGetTime().toFloat();
        }
    }

    private fun dispose() {
        /* Free buffers */
        glfwFreeCallbacks(windowReference);
        glfwDestroyWindow(windowReference);

        glfwTerminate();
        glfwSetErrorCallback(null)?.free();
    }
}