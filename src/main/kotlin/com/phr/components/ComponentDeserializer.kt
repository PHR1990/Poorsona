package com.phr.components

import com.google.gson.*
import java.lang.reflect.Type
import java.util.*

class ComponentDeserializer : JsonSerializer<Component>, JsonDeserializer<Component>{

    override fun deserialize(jsonElement: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Component {

        val jsonObject = jsonElement!!.asJsonObject;
        val type = jsonObject.get("type").asString


        val element = jsonObject.get("properties");


        var component : Component =  context!!.deserialize(element, Class.forName(type))

        return component;

    }

    override fun serialize(source: Component?, typeOfSource: Type?, context: JsonSerializationContext?): JsonElement {

        val result = JsonObject();

        result.add("type", JsonPrimitive(source!!.javaClass.canonicalName))
        result.add("properties", context!!.serialize(source, source.javaClass));
        return result;
    }
}