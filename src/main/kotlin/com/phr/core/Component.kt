package com.phr.core

abstract class Component {

    lateinit var gameObject: GameObject;

    abstract fun update(deltaTime : Float);

    open fun start() {

    }
}