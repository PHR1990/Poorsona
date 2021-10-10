package com.phr.core

abstract class Component {

    @Transient lateinit var gameObject: GameObject;

    open fun update(deltaTime : Float) {

    }

    open fun imGui() {

    }

    open fun start() {

    }
}