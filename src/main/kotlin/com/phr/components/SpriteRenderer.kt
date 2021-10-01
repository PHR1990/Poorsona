package com.phr.components

import com.phr.core.Component
import com.phr.renderer.Texture
import org.joml.Vector2f
import org.joml.Vector4f

class SpriteRenderer(color: Vector4f = Vector4f(1f, 1f, 1f, 1f), texture: Texture? = null) : Component() {

    var color: Vector4f = color;
    var textCoordinates: List<Vector2f> = ArrayList();

    var texture: Texture? = texture
        private set


    override fun start() {

    }

    override fun update(deltaTime: Float) {

    }

    fun getTextureCoordinates() : List<Vector2f> {
        return listOf(
            Vector2f(1f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f),
        );
    }
}