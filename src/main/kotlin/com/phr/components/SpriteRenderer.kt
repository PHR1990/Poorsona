package com.phr.components

import com.phr.core.Component

class SpriteRenderer : Component() {

    var firstTime = false;

    override fun start() {
        println("startiung");
    }

    override fun update(deltaTime: Float) {
        if (!firstTime) {
            println("Updating");
        }
        firstTime = true;
    }
}