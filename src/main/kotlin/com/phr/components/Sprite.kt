package com.phr.components

import com.phr.renderer.Texture
import com.phr.util.TextureConstants
import org.joml.Vector2f

 data class Sprite (var texture: Texture? = null
 , var textureCoordinates: List<Vector2f> = TextureConstants.DEFAULT_TEXTURE_COORDINATES()
                    , var width:Int = 0, var height:Int = 0)

