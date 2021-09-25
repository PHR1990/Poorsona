package com.phr.core

import com.phr.renderer.Camera

abstract class Scene {

    protected lateinit var camera: Camera;

    abstract fun update(elapsedTimeInNano: Float) : Unit;

    abstract fun init();
}