package com.phr.components

import com.phr.core.Component
import com.phr.core.Transform
import com.phr.renderer.Texture
import com.phr.util.TextureConstants
import imgui.ImGui
import org.joml.Vector2f
import org.joml.Vector4f
import java.util.*

class SpriteRenderer(sprite : Sprite = Sprite(), color: Vector4f = Vector4f(1f, 1f, 1f, 1f)) : Component() {

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

    @Transient var isDirty = true;

    @Transient lateinit var lastTransform: Transform;



    override fun start() {
        this.lastTransform = gameObject.transform.copy();
    }

    override fun update(deltaTime: Float) {
        if (lastTransform != gameObject.transform) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    override fun imGui() {
        val imColor = floatArrayOf(color.x, color.y, color.z, color.w);

        if (ImGui.colorPicker4("Color picker", imColor)) {
            this.color.x = imColor[0];
            this.color.y = imColor[1];
            this.color.z = imColor[2];
            this.color.w = imColor[3];
            this.isDirty = true;
        }
    }

    fun getTextureCoordinates() : List<Vector2f> {
        return sprite.textureCoordinates;
    }


}