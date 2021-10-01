package com.phr.renderer

import com.phr.components.SpriteRenderer
import com.phr.core.GameObject

class Renderer {

    val MAX_BATCH_SIZE = 1000;
    var batches: ArrayList<RenderBatch> = ArrayList();

    fun add(gameObject: GameObject) {
        val sprite: SpriteRenderer? = gameObject.getComponent(SpriteRenderer::class.java);

        if (sprite != null) {
            add(sprite);
        }
    }

    fun add(sprite:SpriteRenderer) {

        var wasAdded = false;

        batches.forEach { batch : RenderBatch ->
            if (batch.hasRoom) {
                batch.addSprite(sprite);
                wasAdded = true;
                return;
            }
        }

        if (!wasAdded) {
            val newBatch = RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
        }
    }

    fun render() {
        batches.forEach{it.render()};
    }
}