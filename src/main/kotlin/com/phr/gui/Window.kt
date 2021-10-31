package com.phr.gui

import com.phr.scenes.LevelEditorScene
import com.phr.scenes.LevelScene
import com.phr.pong.PongScene
import com.phr.scenes.Scene
import com.phr.io.KeyListener
import com.phr.io.MouseListener
import com.phr.renderer.DebugDraw
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil

object Window {
    var windowReference: Long = 0

    var width = 800f
        private set;

    var height = 600f
        private set;

    private val title = "com.phr.gui.Window test";

    private var imGuiLayer = ImGuiLayer;

    lateinit var currentScene: Scene
        private set

    var red = 1f
    var green = 1f
    var blue = 1f
    var alpha = 1f

    var imGuiEnabled = true;

    private fun changeScene(newSceneIndex : Int) : Unit {
        currentScene = when (newSceneIndex) {
            0 -> {
                imGuiEnabled = true;
                LevelEditorScene()
            };
            1 -> LevelScene();
            2 -> {
                imGuiEnabled = false;
                PongScene()
            };
            else -> {
                throw Error("Unknown scene");
            }
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();

    }

    fun run() {
        initializeOpenGlWindow()

        initializeListeners()

        changeScene(0);

        //if (imGuiEnabled) {
        imGuiLayer.initializeImGui(windowReference);
        //}

        gameLoop()

        currentScene.saveExit();

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
        windowReference = glfwCreateWindow(width.toInt(), height.toInt(), title, MemoryUtil.NULL, MemoryUtil.NULL);

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

        glEnable(GL_BLEND);

        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
    }

    private fun initializeListeners() {
        glfwSetCursorPosCallback(windowReference, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(windowReference, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(windowReference, MouseListener::mouseScrollCallback);

        glfwSetKeyCallback(windowReference, KeyListener::keyPressedCallback)

        glfwSetWindowSizeCallback(windowReference, Window::resizeWindowCallback);

    }

    private fun resizeWindowCallback(window: Long, newWidth: Int, newHeight: Int) {
        this.width = newWidth.toFloat();
        this.height = newHeight.toFloat();
    }

    fun gameLoop() {

        var beginTime: Float = glfwGetTime().toFloat();
        var deltaTime: Float = -1f;

        while (!glfwWindowShouldClose(windowReference)) {

            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(red, green, blue, alpha);
            glClear(GL_COLOR_BUFFER_BIT);

            if (deltaTime >= 0) {
                DebugDraw.draw();
                currentScene.update(deltaTime);
            }

            if (imGuiEnabled) {
                imGuiLayer.update(deltaTime, currentScene)
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