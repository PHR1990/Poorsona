package com.phr.renderer

import org.joml.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL20.*
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class Shader constructor(filePath: String) {

    private var shaderProgramId = 0;

    private lateinit var vertexSource:String;
    private lateinit var fragmentSource: String;
    val FRAGMENT = "fragment";
    val VERTEX = "vertex";

    init {
        val source = String(Files.readAllBytes(Paths.get(filePath)));
        val splitString: List<String> = source.split(Regex("(#type)( )+([a-zA-Z]+)"));

        // Finding first '#type pattern'
        var index = source.indexOf("#type") + 6;
        var endOfLine = source.indexOf("\r\n", index);

        val firstPattern = source.substring(index, endOfLine).trim();

        // Finding second '#type pattern'
        index = source.indexOf("#type", endOfLine) + 6;
        endOfLine = source.indexOf("\r\n", index);

        val secondPattern = source.substring(index, endOfLine).trim();

        if (VERTEX.equals(firstPattern)) {
            vertexSource = splitString[1];
        } else if (FRAGMENT.equals(firstPattern)) {
            fragmentSource = splitString[1];
        } else {
            throw IOException("Couldnt properly load  shader." + firstPattern);
        }

        if (VERTEX.equals(secondPattern)) {
            vertexSource = splitString[2]; // TODO era 1
        } else if (FRAGMENT.equals(secondPattern)) {
            fragmentSource = splitString[2]; // TODO era 1
        } else {
            throw IOException("Couldnt properly load  shader." + secondPattern);
        }

    }

    fun compileAndLink() {
        var vertexId = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexId, vertexSource);
        glCompileShader(vertexId);

        // check errors
        if (glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {

            println("ERROR COMPILING SHADER= "+ glGetShaderInfoLog(
                vertexId,
                glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)
            )
            );

        }

        var fragmentId = glCreateShader(GL20.GL_FRAGMENT_SHADER);

        glShaderSource(fragmentId, fragmentSource);
        glCompileShader(fragmentId);

        // check errors
        if (glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            println("ERROR COMPILING SHADER= "+ glGetShaderInfoLog(
                fragmentId,
                glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)
            )
            );

        }

        // link shaders check errors
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        if (GL_FALSE == glGetProgrami(shaderProgramId, GL_LINK_STATUS)) {
            //val len: Int = GL20.glGetProgrami(shaderProgram, GL20.GL_INFO_LOG_LENGTH);
            println("ERROR LINKING= "+glGetProgramInfoLog(shaderProgramId, glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH)));
            //println("Error linking =" + GL20.glGetProgramInfoLog(shaderProgram, len));
        }
    }

    fun use() {
        glUseProgram(shaderProgramId);
    }

    fun detach(): Unit {
        glUseProgram(0);
    }

    fun uploadMat4f(varName: String, mat4Value: Matrix4f) {

        val varLocation = glGetUniformLocation(shaderProgramId, varName);
        val matBuffer = BufferUtils.createFloatBuffer(16);
        mat4Value.get(matBuffer)
        glUniformMatrix4fv(varLocation, false, matBuffer);

    }

}