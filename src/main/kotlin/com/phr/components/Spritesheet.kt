package com.phr.components

import com.phr.renderer.Texture
import org.joml.Vector2f

data class Spritesheet (var texture: Texture, var spriteWidth: Int, var spriteHeight : Int
    ,var numberSprites: Int, var spacing : Int ){

    var sprites = ArrayList<Sprite>();

    init {
        var currentX = 0;
        var currentY = texture.height - spriteHeight;

        for (i in 0 until numberSprites) {
            val topY : Float = (currentY + spriteHeight) / texture.height.toFloat();
            val rightX : Float = (currentX + spriteWidth) / texture.width.toFloat();
            val leftX : Float = currentX / texture.width.toFloat();
            val bottomY : Float = currentY / texture.height.toFloat();

            val textureCoordinates: List<Vector2f> = listOf(
                Vector2f(rightX, topY),
                Vector2f(rightX, bottomY),
                Vector2f(leftX, bottomY),
                Vector2f(leftX, topY),
            );

            val sprite = Sprite(this.texture, textureCoordinates, spriteWidth, spriteHeight);
            sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if (currentX > texture.width) {
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }

        }

    }
    /*lateinit var texture: Texture;

    lateinit var sprites: List<Sprite>;
*/

}