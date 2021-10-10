package com.phr.components

import com.phr.core.Component
import org.joml.Vector3f
import org.joml.Vector4f

class RigidBody : Component() {

    private var colliderType = 0;

    private var friction = 0.8f;

    private var velocity = Vector3f(0f, 0.5f, 0f);

    private var isBoxCollider = false;

    @Transient var tmp = Vector4f(0f, 0f, 0f, 0f);
}