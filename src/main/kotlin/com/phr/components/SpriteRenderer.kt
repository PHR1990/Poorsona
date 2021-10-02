package com.phr.components

import com.phr.core.Component
import com.phr.renderer.Texture
import com.phr.util.TextureConstants
import org.joml.Vector2f
import org.joml.Vector4f
import java.util.*

class SpriteRenderer(sprite : Sprite, color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Component() {

    constructor(color: Vector4f) : this(Sprite(null), color)

    var color: Vector4f = color;

    var sprite: Sprite = sprite;


    override fun start() {

    }

    override fun update(deltaTime: Float) {

    }

    fun getTextureCoordinates() : List<Vector2f> {
        return sprite.textureCoordinates;
    }
}