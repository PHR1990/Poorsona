package com.phr.gui

import com.phr.scenes.Scene
import com.phr.gui.Window.height
import com.phr.gui.Window.width
import com.phr.gui.Window.windowReference
import com.phr.io.KeyListener
import com.phr.io.MouseListener
import imgui.ImFontConfig
import imgui.ImGui
import imgui.callback.ImStrConsumer
import imgui.callback.ImStrSupplier
import imgui.flag.*
import imgui.gl3.ImGuiImplGl3
import org.lwjgl.glfw.GLFW.*


object ImGuiLayer {

    var glfwWindow : Long = 0;

    private val mouseCursors = LongArray(ImGuiMouseCursor.COUNT)

    val imGuiGl3 = ImGuiImplGl3();

    private val imGuiConfigurationFile = "imgui.ini"

    fun initializeImGui(glfwWindow : Long) {
        this.glfwWindow = glfwWindow;

        ImGui.createContext();

        val io = ImGui.getIO();

        io.iniFilename = imGuiConfigurationFile;

        io.configFlags = ImGuiConfigFlags.NavEnableKeyboard;
        io.backendFlags = ImGuiBackendFlags.HasMouseCursors;
        io.backendPlatformName = "imgui_java_impl_glfw";


        // ------------------------------------------------------------
        // Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        val keyMap = IntArray(ImGuiKey.COUNT)
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME
        keyMap[ImGuiKey.End] = GLFW_KEY_END
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER
        keyMap[ImGuiKey.A] = GLFW_KEY_A
        keyMap[ImGuiKey.C] = GLFW_KEY_C
        keyMap[ImGuiKey.V] = GLFW_KEY_V
        keyMap[ImGuiKey.X] = GLFW_KEY_X
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z
        io.setKeyMap(keyMap)


        // ------------------------------------------------------------
        // Mouse cursors mapping

        // ------------------------------------------------------------
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR)
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR)


        // ------------------------------------------------------------
        // GLFW callbacks to handle user input
        glfwSetKeyCallback(glfwWindow) { w: Long, key: Int, scancode: Int, action: Int, mods: Int ->
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true)
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false)
            }
            io.keyCtrl = io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL)
            io.keyShift = io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT)
            io.keyAlt = io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT)
            io.keySuper = io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER)

            if(!io.wantCaptureKeyboard) {
                KeyListener.keyPressedCallback(windowReference, key,scancode, action, mods);
            }
        }

        glfwSetCharCallback(glfwWindow) { w: Long, c: Int ->
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c)
            }
        }

        glfwSetMouseButtonCallback(glfwWindow) { w: Long, button: Int, action: Int, mods: Int ->
            val mouseDown = BooleanArray(5)
            mouseDown[0] =
                button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE
            mouseDown[1] =
                button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE
            mouseDown[2] =
                button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE
            mouseDown[3] =
                button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE
            mouseDown[4] =
                button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE
            io.setMouseDown(mouseDown)
            if (!io.wantCaptureMouse && mouseDown[1]) {
                ImGui.setWindowFocus(null)
            }

            if (!io.wantCaptureMouse) {
                MouseListener.mouseButtonCallback(windowReference, button, action, mods);
            }
        }

        glfwSetScrollCallback(glfwWindow) { w: Long, xOffset: Double, yOffset: Double ->
            io.mouseWheelH = io.mouseWheelH + xOffset.toFloat()
            io.mouseWheel = io.mouseWheel + yOffset.toFloat()
        }

        io.setSetClipboardTextFn(object : ImStrConsumer() {
            override fun accept(s: String) {
                glfwSetClipboardString(glfwWindow, s)
            }
        })

        io.setGetClipboardTextFn(object : ImStrSupplier() {
            override fun get(): String {
                val clipboardString = glfwGetClipboardString(glfwWindow)
                return clipboardString ?: ""
            }
        })


        // ------------------------------------------------------------
        // Fonts configuration
        // Read: https://raw.githubusercontent.com/ocornut/imgui/master/docs/FONTS.txt

        val fontAtlas = io.fonts;
        val fontConfig = ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.glyphRanges = fontAtlas.glyphRangesDefault;

        // Add a default font, which is 'ProggyClean.ttf, 13px'
        // fontAtlas.addFontDefault();

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/segoeui.ttf", 18f, fontConfig);


        fontConfig.destroy(); // After all fonts were added we don't need this config more

        // ------------------------------------------------------------
        // Use freetype instead of stb_truetype to build a fonts texture

        //ImGuiFreeType.buildFontAtlas(fontAtlas, ImGuiFreeType.RasterizerFlags.LightHinting);

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init("#version 330 core")

    }

    fun update(dt: Float, currentScene: Scene) {
        startFrame(dt)

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        ImGui.newFrame()
        currentScene.sceneImGui();
        ImGui.showDemoWindow()
        ImGui.render()
        endFrame()
    }

    private fun startFrame(deltaTime: Float) {
        // Get window properties and mouse position
        val winWidth = floatArrayOf(width.toFloat())
        val winHeight = floatArrayOf(height.toFloat())
        val mousePosX = doubleArrayOf(0.0)
        val mousePosY = doubleArrayOf(0.0)
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY)

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        val io = ImGui.getIO()
        io.setDisplaySize(winWidth[0], winHeight[0])
        io.setDisplayFramebufferScale(1f, 1f)
        io.setMousePos(mousePosX[0].toFloat(), mousePosY[0].toFloat())
        io.deltaTime = deltaTime

        // Update the mouse cursor
        val imguiCursor = ImGui.getMouseCursor()
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor])
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL)
    }

    private fun endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        imGuiGl3.renderDrawData(ImGui.getDrawData())
    }

    private fun destroyImGui() {
        imGuiGl3.dispose()
        ImGui.destroyContext()
    }
}