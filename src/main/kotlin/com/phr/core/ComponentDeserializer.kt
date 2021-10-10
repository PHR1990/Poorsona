package com.phr.core

import com.google.gson.*
import java.lang.reflect.Type

class ComponentDeserializer : JsonSerializer<Component>, JsonDeserializer<Component>{

    override fun deserialize(jsonElement: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Component {

        val jsonObject = jsonElement!!.asJsonObject;
        val type = jsonObject.get("type").asString

        val element = jsonObject.get("properties");

        return context!!.deserialize(element, Class.forName(type))

    }

    override fun serialize(source: Component?, typeOfSource: Type?, context: JsonSerializationContext?): JsonElement {

        val result = JsonObject();

        result.add("type", JsonPrimitive(source!!.javaClass.canonicalName))
        result.add("properties", context!!.serialize(source, source.javaClass));
        return result;
    }
}