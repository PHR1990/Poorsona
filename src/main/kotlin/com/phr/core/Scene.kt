package com.phr.core

abstract class Scene {


    abstract fun update(elapsedTimeInNano: Float) : Unit;

    abstract fun init();
}