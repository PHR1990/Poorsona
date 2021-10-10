package com.phr.core

import imgui.ImGui
import org.joml.Vector3f
import org.joml.Vector4f
import java.lang.reflect.Field
import java.lang.reflect.Modifier

abstract class Component {

    @Transient lateinit var gameObject: GameObject;

    open fun update(deltaTime : Float) {

    }

    open fun imGui() {
        val fields = this::class.java.declaredFields;

        fields.forEach {
            field : Field ->

            if (Modifier.isTransient(field.modifiers)) {
                return@forEach;
            }

            val isPrivate = Modifier.isPrivate(field.modifiers);

            if (isPrivate) {
                field.isAccessible = true;
            }

            val type = field.type;
            val value = field.get(this);
            val name = field.name;
            if (type == Int::class.java) {

                var intArr : IntArray = intArrayOf(value as Int);

                if (ImGui.dragInt("$name: ", intArr)) {
                    field.set(this, intArr.get(0));
                }
            } else if (type == Float::class.java) {
                var floatArr = floatArrayOf(value as Float);

                if (ImGui.dragFloat("$name: ", floatArr)) {
                    field.set(this, floatArr.get(0));
                }
            } else if (type == Boolean::class.java) {

                if (ImGui.checkbox("$name: ", value as Boolean)) {
                    field.set(this, !value as Boolean);
                }
            } else if (type == Vector3f::class.java) {
                val valAsVector3f = value as Vector3f
                val vector3fArray = floatArrayOf(valAsVector3f.x, valAsVector3f.y, valAsVector3f.z);

                if (ImGui.dragFloat3(name + ": ", vector3fArray)) {
                    valAsVector3f.x = vector3fArray.get(0);
                    valAsVector3f.y = vector3fArray.get(1);
                    valAsVector3f.z = vector3fArray.get(2);
                }
            } else if (type == Vector4f::class.java) {
                val valAsVector4f = value as Vector4f
                val vector4fArray = floatArrayOf(valAsVector4f.x, valAsVector4f.y, valAsVector4f.z, valAsVector4f.w);

                if (ImGui.dragFloat3(name + ": ", vector4fArray)) {
                    valAsVector4f.x = vector4fArray.get(0);
                    valAsVector4f.y = vector4fArray.get(1);
                    valAsVector4f.z = vector4fArray.get(2);
                    valAsVector4f.w = vector4fArray.get(3);
                }
            }

            if (isPrivate) {
                field.isAccessible = false;
            }
        }
    }

    open fun start() {

    }
}