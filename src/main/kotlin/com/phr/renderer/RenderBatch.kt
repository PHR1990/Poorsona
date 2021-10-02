package com.phr.renderer

import com.phr.components.SpriteRenderer
import com.phr.gui.Window
import com.phr.util.AssetPool
import org.joml.Vector2f

import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glDisableVertexAttribArray
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.glBindVertexArray
import org.lwjgl.opengl.GL30.glGenVertexArrays


class RenderBatch (maxBatchSize: Int) {
    // Vertex
    // Pos                  Color                           textCoords      text Id
// float, float,        Float, float, float, float      float, float        float

    private val POS_SIZE = 2;
    private val COLOR_SIZE = 4;

    private val TEXTURE_COORDINATES_SIZE = 2;
    private val TEXTURE_ID_SIZE = 1;

    private val POS_OFFSET = 0;
    private val COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.SIZE_BYTES;
    private val TEXTURE_COORDINATES_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.SIZE_BYTES
    private val TEXTURE_ID_OFFSET = TEXTURE_COORDINATES_OFFSET + TEXTURE_COORDINATES_SIZE * Float.SIZE_BYTES

    private val VERTEX_SIZE = 9;
    private val VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.SIZE_BYTES;

    private var sprites = arrayOfNulls<SpriteRenderer>(maxBatchSize);
    private var numberSprites: Int = 0;
    var hasRoom: Boolean = true
        private set;
    private var vertices = FloatArray (maxBatchSize * 4 * VERTEX_SIZE);
    private val textureSlots = intArrayOf (0,1, 2,3,4,5,6,7);

    private var textures = ArrayList<Texture>();
    private var vaoId = 0;
    private var vboId = 0;

    var maxBatchSize = maxBatchSize;

    private var shader = AssetPool.getShader("assets/shaders/default.glsl");


    fun start() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertices.size.toLong() * Float.SIZE_BYTES, GL_DYNAMIC_DRAW);

        val eboId: Int = glGenBuffers();
        val indices : IntArray = generateIndices()
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET.toLong());
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET.toLong())
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, TEXTURE_COORDINATES_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDINATES_OFFSET.toLong())
        glEnableVertexAttribArray(2)

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET.toLong())
        glEnableVertexAttribArray(3)

    }

    fun addSprite(spriteRenderer: SpriteRenderer) {
        val index: Int = this.numberSprites;

        this.sprites[index] = spriteRenderer;
        this.numberSprites++;

        if (spriteRenderer.sprite.texture != null) {
            if (!textures.contains(spriteRenderer.sprite.texture)) {
                textures.add(spriteRenderer.sprite.texture!!);
                // TODO if we have more than 8, create a new batch
            }
        }

        loadVertexProperties(index);

        if (numberSprites >= this.maxBatchSize) {
            this.hasRoom = false;
        }
    }

    private fun loadVertexProperties(index: Int) {
        val spriteRenderer: SpriteRenderer = sprites[index]!!; // Unexpected null, explode

        // float float  float float float float (position   color)

        var offset = index * 4 * VERTEX_SIZE;

        var textureId = 0;
        if (spriteRenderer.sprite.texture != null) {
            for (i in 0 until textures.size) {
                if (textures.get(i) == spriteRenderer.sprite.texture) {
                    textureId = i + 1;
                    break;
                }
            }
        }

        setSpritePositionAndColorIntoVertices(spriteRenderer, 1f, 1f, offset, textureId, spriteRenderer.getTextureCoordinates()[0]);
        offset+= VERTEX_SIZE;
        setSpritePositionAndColorIntoVertices(spriteRenderer, 1f, 0f, offset, textureId, spriteRenderer.getTextureCoordinates()[1]);
        offset+= VERTEX_SIZE;
        setSpritePositionAndColorIntoVertices(spriteRenderer, 0f, 0f, offset, textureId, spriteRenderer.getTextureCoordinates()[2]);
        offset+= VERTEX_SIZE;
        setSpritePositionAndColorIntoVertices(spriteRenderer, 0f, 1f, offset, textureId, spriteRenderer.getTextureCoordinates()[3]);
        offset+= VERTEX_SIZE;

    }

    private fun setSpritePositionAndColorIntoVertices
                (sprite: SpriteRenderer,xAdd: Float, yAdd: Float, offset: Int, textureId : Int, textureCoordinates : Vector2f) {

        // position
        vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
        vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

        // color
        vertices[offset + 2] = sprite.color.x;
        vertices[offset + 3] = sprite.color.y;
        vertices[offset + 4] = sprite.color.z;
        vertices[offset + 5] = sprite.color.w;

        // Load texture coordinates
        vertices[offset + 6] = textureCoordinates.x;
        vertices[offset + 7] = textureCoordinates.y;
        // Load texture ID
        vertices[offset + 8] = textureId.toFloat();
    }

    fun render() {
        var rebufferData = true;
        for (i in 0 until numberSprites) {
            val sprite = sprites.get(i);
            if (sprite!!.isDirty) {
                loadVertexProperties(i);
                sprite!!.isDirty = false;

                rebufferData = true;
            }
        }
        if (rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }
        // For now we will rebuffer all data every frame


        // use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.currentScene.camera.getProjectionMatrix());
        shader.uploadMat4f("uView", Window.currentScene.camera.getViewMatrix());

        for (i in 0 until textures.size) {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }

        shader.uploadIntArray("uTextures", textureSlots);

        glBindVertexArray(vaoId)

        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        glDrawElements(GL_TRIANGLES, this.numberSprites * 6, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)

        glBindVertexArray(0)

        for (i in 0 until textures.size) {
            textures.get(i).unbind();
        }

        shader.detach();
    }

    private fun generateIndices(): IntArray {

        // 6 indixes per quad (3 per triangle)
        val elements = IntArray(6 * maxBatchSize);

        for (i in 0 until maxBatchSize) {
            loadVerticesAsSquare(elements, i);
        }

        return elements;
    }

    private fun loadVerticesAsSquare(elements: IntArray, index: Int) {

        val offsetArrayIndex = 6 * index;
        val offset = 4 * index;
        // Example of Vertices Points:
        // No Offset (0)            Second Triangle w/ offset (1)
        // 3, 2, 0, 0, 2, 1         7, 6, 4, 4, 6, 5
        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    fun hasTextureRoom(): Boolean {
        return textures.size < 8;
    }

    fun hasTexture(texture: Texture): Boolean {
        return textures.contains(texture);
    }

}