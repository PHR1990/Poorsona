package com.phr.core

import org.joml.Vector2f

data class Transform (var position: Vector2f = Vector2f(), var scale: Vector2f = Vector2f()) {

    fun copy(to: Transform) {
        to.position = Vector2f(position.x, position.y);
        to.scale = Vector2f(scale.x, scale.y)
    }
    @Override
    fun copy() : Transform {
        return Transform(Vector2f(position.x, position.y), Vector2f(scale.x, scale.y));
    }
}