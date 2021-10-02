package com.phr.components

import com.phr.core.Component
import com.phr.core.Transform
import com.phr.renderer.Texture
import com.phr.util.TextureConstants
import org.joml.Vector2f
import org.joml.Vector4f
import java.util.*

class SpriteRenderer(sprite : Sprite, color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Component() {

    constructor(color: Vector4f) : this(Sprite(null), color)

    var color: Vector4f = color
        set(value) {
            if (color != value) {
                field = value;
                isDirty = true;
            }

        }

    var sprite: Sprite = sprite
        set(value) {
            isDirty = true;
            field = value
        }

    var isDirty = true;

    lateinit var lastTransform: Transform;


    override fun start() {
        this.lastTransform = gameObject.transform.copy();
    }

    override fun update(deltaTime: Float) {
        if (lastTransform != gameObject.transform) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    fun getTextureCoordinates() : List<Vector2f> {
        return sprite.textureCoordinates;
    }


}