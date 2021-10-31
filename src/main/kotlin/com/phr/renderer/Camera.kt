package com.phr.renderer

import org.joml.Matrix4f
import org.joml.Vector2f
import org.joml.Vector3f

class Camera (position: Vector2f) {

    private var projectionMatrix: Matrix4f = Matrix4f();
    private var viewMatrix: Matrix4f = Matrix4f();
    val projectionSize = Vector2f(32.0f * 40f, 32f * 21f);

    var inverseProjection = Matrix4f()
        private set


    var inverseView = Matrix4f()
        private set


    var position: Vector2f = position;

    init {
        adjustProjection();
    }

    private fun adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x, 0f, projectionSize.y , 0f, 100f);
        projectionMatrix.invert(inverseProjection);
    }

    fun getViewMatrix(): Matrix4f {
        val cameraFront = Vector3f(0f,0f,-1f);
        val cameraUp = Vector3f(0f, 1f, 0f);

        viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(
            Vector3f(position.x, position.y, 20f)
            , cameraFront.add(position.x, position.y, 0f)
            , cameraUp
        );
        viewMatrix.invert(inverseView);

        return this.viewMatrix;

    }

    fun getProjectionMatrix(): Matrix4f {
        return projectionMatrix;
    }

}
