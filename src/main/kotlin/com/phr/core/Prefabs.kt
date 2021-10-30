package com.phr.core

import com.phr.components.Sprite
import com.phr.components.SpriteRenderer
import org.joml.Vector2f

object Prefabs {

    fun generateSpriteObject(sprite: Sprite, sizeX : Float, sizeY : Float) : GameObject {
        val block = GameObject("Sprite_Object_Gen"
        ,  Transform(Vector2f(), Vector2f(sizeX, sizeY)), 0);
        val renderer = SpriteRenderer(sprite);

        block.addComponent(renderer);

        return block;
    }
}