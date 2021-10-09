package com.phr.core

abstract class Component {

    lateinit var gameObject: GameObject;

    open fun update(deltaTime : Float) {

    }

    open fun imGui() {

    }

    open fun start() {

    }
}