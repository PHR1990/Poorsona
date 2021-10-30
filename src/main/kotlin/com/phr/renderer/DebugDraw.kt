package com.phr.renderer

import com.phr.gui.Window
import com.phr.util.AssetPool
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.opengl.GL30.*
import java.util.*

object DebugDraw {

    val MAX_LINES = 500;

    var lines = ArrayList<Line2D>();

    val vertexArray = FloatArray(MAX_LINES * 6 * 2);

    val shader = AssetPool.getShader("assets/shaders/debugLine2D.glsl")

    var vaoId : Int = 0;
    var vboId : Int = 0;

    var started = false;

    fun start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexArray.size.toLong() * Float.SIZE_BYTES, GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.SIZE_BYTES, 3 * Float.SIZE_BYTES.toLong());
        glEnableVertexAttribArray(1);

        // Set line width
        glLineWidth(4f)
    }

    fun beginFrame() {
        if (!started) {
            start();
            started = true;
        }

        // remove dead lines
        var deadLinesIndexes = ArrayList<Int>();
        for (i in 0 until lines.size - 1) {
            if (lines[i].beginFrame() < 0) {
                deadLinesIndexes.add(i);
            }
        }

        deadLinesIndexes.forEach {
            lines.removeAt(it);
        }

    }

    fun draw() {
        var index = 0;
        lines.forEach {
            for (i in 0 until 2) {
                val position = if (i == 0) it.from else it.to;
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10f;

                vertexArray[index + 3] = it.color.x;
                vertexArray[index + 4] = it.color.y;
                vertexArray[index + 5] = it.color.z;

                index+= 6;
            }
        }

        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size * 6 * 2))

        shader.use();
        shader.uploadMat4f("uProjection", Window.currentScene.camera.getProjectionMatrix())
        shader.uploadMat4f("uView", Window.currentScene.camera.getViewMatrix())

        glBindVertexArray(vaoId)
        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawArrays(GL_LINES, 0, lines.size * 6 * 2);

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)

        glBindVertexArray(0)

        shader.detach();
    }

    fun addLine2D(from : Vector2f, to : Vector2f) {

        addLine2D(from, to, Vector3f(0f, 1f,0f ),300)
    }

    fun addLine2D(from : Vector2f, to : Vector2f, color: Vector3f, lifetime : Int) {
        if (lines.size > MAX_LINES) return;
        lines.add(Line2D(from, to, color, lifetime));
    }
}