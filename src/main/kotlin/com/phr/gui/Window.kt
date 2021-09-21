package com.phr.gui

import com.phr.core.LevelEditorScene
import com.phr.core.LevelScene
import com.phr.core.Scene
import com.phr.io.KeyListener
import com.phr.io.MouseListener
import com.phr.util.Time
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.system.MemoryUtil

object Window {
    private var windowReference: Long = 0;

    private val width = 800;
    private val height = 600;

    private val title = "com.phr.gui.Window test";

    private lateinit var currentScene: Scene;

    var red = 1f
    var green = 1f
    var blue = 1f
    var alpha = 1f

    fun changeScene(newSceneIndex : Int) : Unit {
        when (newSceneIndex) {
            0 -> currentScene = LevelEditorScene();
            1 -> currentScene = LevelScene();
            else -> {
                throw Error("Unknown scene");
            }
        }
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
        glfwWindowHint(GLFW_VISIBLE, GLFW.GLFW_FALSE);
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

        var beginTime: Float = Time.getTimeInSeconds();
        var elapsedTimeInSeconds: Float = -1f;
        while (!glfwWindowShouldClose(windowReference)) {

            glfwPollEvents();

            GL11.glClearColor(red, green, blue, alpha);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            if (elapsedTimeInSeconds >= 0) {
                currentScene.update(elapsedTimeInSeconds);
            }

            glfwSwapBuffers(windowReference)

            elapsedTimeInSeconds = Time.getTimeInSeconds() - beginTime;

            beginTime = Time.getTimeInSeconds();
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