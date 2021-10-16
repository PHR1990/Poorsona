package com.phr.renderer

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11.*
import org.lwjgl.stb.STBImage.*
import java.nio.ByteBuffer
import java.nio.IntBuffer

class Texture (filePath: String) {

    private val filePath: String = filePath;
    val textureId: Int = glGenTextures()

    var width = 0
        private set;

    var height = 0
        private set;

    init {
        glBindTexture(GL_TEXTURE_2D, textureId);

        // WRAP S = behaviour on coordinate X
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        // WRAP T = behaviour on coordinate Y
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // when stretching, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        // when shrinking, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        stbi_set_flip_vertically_on_load(true)
        val width: IntBuffer = BufferUtils.createIntBuffer(1);
        val height: IntBuffer = BufferUtils.createIntBuffer(1);
        val channels: IntBuffer = BufferUtils.createIntBuffer(1);


        val image: ByteBuffer? = stbi_load(filePath, width, height, channels, 0);

        if (image != null) {
            this.width = width.get(0);
            this.height = height.get(0);

            if (channels.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0)
                    , height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (channels.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0)
                    , height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                throw RuntimeException("Unkown number of channels" + channels.get(0));
            }

        } else {
            throw RuntimeException("Could not load texture");
        }

        stbi_image_free(image);
    }

    fun bind() {
        glBindTexture(GL_TEXTURE_2D, textureId);
    }

    fun unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}