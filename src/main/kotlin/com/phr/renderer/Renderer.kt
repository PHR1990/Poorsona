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

    fun add(sproteRenderer:SpriteRenderer) {

        var wasAdded = false;

        batches.forEach { batch : RenderBatch ->
            if (batch.hasRoom) {
                val texture = sproteRenderer.sprite.texture;
                if (texture == null ||(batch.hasTexture(texture) || batch.hasTextureRoom())) {

                    batch.addSprite(sproteRenderer);
                    wasAdded = true;
                    return;
                }

            }
        }

        if (!wasAdded) {
            val newBatch = RenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sproteRenderer);
        }
    }

    fun render() {
        batches.forEach{it.render()};
    }
}