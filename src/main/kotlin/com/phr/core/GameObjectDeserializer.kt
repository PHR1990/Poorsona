package com.phr.core

import com.google.gson.*
import com.phr.components.Component
import java.lang.reflect.Type
import java.util.*

class GameObjectDeserializer : JsonDeserializer<GameObject> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): GameObject {

        val jsonObject = json!!.asJsonObject;
        val name = jsonObject.get("name").asString;

        val components = jsonObject.getAsJsonArray("components")

        val transform : Transform = context!!.deserialize(jsonObject.get("transform"), Transform::class.java)

        val zIndex = context!!.deserialize<Int>(jsonObject.get("zIndex"), Int::class.java);

        val uuid = jsonObject.get("uuid").asString;

        val gameObject = GameObject(name, transform, zIndex, UUID.fromString(uuid));

        components.forEach {
            val component = context.deserialize<Component>(it, Component::class.java)

            gameObject.addComponent(component);
        }
        return gameObject;
    }


}