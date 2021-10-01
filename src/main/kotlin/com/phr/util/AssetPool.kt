package com.phr.util

import com.phr.renderer.Shader
import com.phr.renderer.Texture
import java.io.File

object AssetPool {

    private val shaderPool : MutableMap<String, Shader> = HashMap();
    private val texturePool : MutableMap<String, Texture> = HashMap();

    fun getShader(resourceName : String): Shader {

        val file = File(resourceName);

        if (shaderPool.containsKey(file.absolutePath)) {
            return shaderPool[file.absolutePath]!!;
        } else {
            val shader = Shader(resourceName);
            shader.compileAndLink();
            this.shaderPool[file.absolutePath] = shader;

            return shader;
        }
    }

    fun getTexture(resourceName: String) : Texture {
        val file = File(resourceName);

        if (texturePool.containsKey(file.absolutePath)) {
            return texturePool[file.absolutePath]!!;
        } else {
            val texture = Texture(resourceName);
            texturePool[file.absolutePath] = texture;

            return texture;
        }
    }

}