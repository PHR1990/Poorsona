package com.phr.renderer

import com.phr.components.SpriteRenderer
import com.phr.core.GameObject
import java.util.*
import kotlin.collections.ArrayList

class Renderer {

    val MAX_BATCH_SIZE = 1000;
    var batches: ArrayList<RenderBatch> = ArrayList();

    fun add(gameObject: GameObject) {
        val sprite: SpriteRenderer? = gameObject.getComponent(SpriteRenderer::class.java);

        if (sprite != null) {
            add(sprite);
        }
    }

    fun add(spriteRenderer:SpriteRenderer) {

        var wasAdded = false;

        batches.forEach { batch : RenderBatch ->
            if (batch.hasRoom && batch.zIndex == spriteRenderer.gameObject.zIndex) {
                val texture = spriteRenderer.sprite.texture;
                if (texture == null ||(batch.hasTexture(texture) || batch.hasTextureRoom())) {

                    batch.addSprite(spriteRenderer);
                    wasAdded = true;
                    return;
                }

            }
        }

        if (!wasAdded) {
            val newBatch = RenderBatch(MAX_BATCH_SIZE, spriteRenderer.gameObject.zIndex);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(spriteRenderer);
            batches.sort();
        }
    }

    fun render() {
        batches.forEach{
            it.render()
        };
    }
}