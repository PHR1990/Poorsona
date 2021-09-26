package com.phr.core

import com.phr.renderer.Camera
import com.phr.renderer.Shader
import com.phr.renderer.Texture
import com.phr.util.Time
import org.joml.Vector2f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays
import java.nio.FloatBuffer
import java.nio.IntBuffer

class LevelEditorScene : Scene() {

    private var vertexArrayObjectId: Int = 0;
    private var vertexBufferObjectId: Int = 0;
    private var elementBufferObjectId: Int =0;

    private var activeShader: Shader;

    private var texture = Texture("assets/images/testImage.png");

    private val DEFAULT_SHADER_PATH = "assets/shaders/default.glsl"

    var vertexArray: FloatArray = floatArrayOf(
        /*Position                  RGB A                       UV Coordinates  */
        100.5f, 0.5f, 0.0f,         1.0f, 0.0f, 1.0f, 1.0f,     1f,1f,// bottom right   0
        0.5f, 100.5f, 0.0f,         0.0f, 1.0f, 1.0f, 1.0f,     0f,0f,// top left       1
        100.5f, 100.5f, 0.0f,       1.0f, 0.0f, 1.0f, 1.0f,     1f,0f,// top right      2
        0.5f, 0.5f, 0.0f,           1.0f, 1.0f, 1.0f, 1.0f,     0f,1f,  //bottom left   3
    );

    var elementArray: IntArray = intArrayOf(
        2, 1, 0,
        0, 1, 3
    );

    init {
        activeShader = Shader(DEFAULT_SHADER_PATH);
    }

    override fun init() {

        activeShader.compileAndLink();

        camera = Camera(Vector2f(-200f, -300f));

        vertexArrayObjectId = glGenVertexArrays();
        glBindVertexArray(vertexArrayObjectId);

        // Create a float buffer of vertices
        var vertexBuffer: FloatBuffer = BufferUtils.createFloatBuffer(vertexArray.size);
        vertexBuffer.put(vertexArray).flip();

        //
        vertexBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferObjectId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create indexes

        var elementBuffer: IntBuffer = BufferUtils.createIntBuffer(elementArray.size);
        elementBuffer.put(elementArray).flip();

        elementBufferObjectId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBufferObjectId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add vertex attribute pointers

        val positionsSize = 3;
        val colorSize = 4;
        val uvSize = 2;
        val vertexSizeBytes = ((positionsSize + colorSize + uvSize) * Float.SIZE_BYTES);
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0)
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize.toLong() * Float.SIZE_BYTES );
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
            (positionsSize.toLong() + colorSize) * Float.SIZE_BYTES);
        glEnableVertexAttribArray(2);
    }

    override fun update(elapsedTimeInSeconds: Float) {

        camera.position.x -= elapsedTimeInSeconds * 5f;

        activeShader.use();

        activeShader.uploadTexture("textureSampler" ,0);
        glActiveTexture(GL_TEXTURE0);
        texture.bind();

        activeShader.uploadMat4f("uProjectionMatrix", camera.getProjectionMatrix());
        activeShader.uploadMat4f("uViewMatrix", camera.getViewMatrix());
        activeShader.uploadFloat("uTime", Time.getTimeInSeconds());
        // bindings
        glBindVertexArray(vertexArrayObjectId);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.size, GL_UNSIGNED_INT, 0)

        // unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        activeShader.detach();

    }
}