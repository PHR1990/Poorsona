package com.phr.core

import com.phr.gui.Window
import com.phr.io.KeyListener
import java.awt.event.KeyEvent

class LevelEditorScene : Scene() {

    var changingScene: Boolean = false;

    var timeChangeScene: Float = 3.0f;

    override fun update(elapsedTimeInSeconds: Float) {

        //println("" + (1.0f/elapsedTimeInSeconds) + " FPS");

        if (KeyListener.keyPressed[KeyEvent.VK_SPACE]) {
            changingScene = true;
        }

        if (changingScene && timeChangeScene > 0) {
            timeChangeScene -= elapsedTimeInSeconds;
            Window.red -= 0.025f;
            Window.blue -= 0.025f;
            Window.green -= 0.025f;

        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}