package com.phr.util

import org.joml.Vector2f

object TextureConstants {

    fun DEFAULT_TEXTURE_COORDINATES() : List<Vector2f> {
        return listOf(
            Vector2f(1f, 1f),
            Vector2f(1f, 0f),
            Vector2f(0f, 0f),
            Vector2f(0f, 1f),
        );
    }

}