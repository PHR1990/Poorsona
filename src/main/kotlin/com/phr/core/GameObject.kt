package com.phr.core

import com.phr.components.Component
import java.rmi.server.UID
import java.util.*
import kotlin.collections.ArrayList

class GameObject (name: String, transform: Transform = Transform(), zIndex: Int = 0, uuid : UUID = UUID.randomUUID()) {

    val uuid = uuid

    private var name: String = name;

    var components: MutableList<Component> = ArrayList()
            private set;

    var transform: Transform = transform;
    var zIndex = zIndex;

    fun <T : Component> getComponent(componentClass : Class<T>): T? {
        components.forEach {
            if (componentClass.isAssignableFrom(it.javaClass)) {
                return componentClass.cast(it);
            }
        };
        return null;
    }

    fun <T : Component> removeComponent(componentClass : Class<T>) {
        components.forEach {
            if (componentClass.isAssignableFrom(it.javaClass)) {
                components.remove(it);
                return@forEach;
            }
        };
    }

    fun addComponent(component : Component) {
        components.add(component);
        component.gameObject = this;
    }

    fun update(deltaTime: Float) {
        components.forEach {
            it.update(deltaTime);
        }
    }

    fun start() {
        components.forEach {
            it.start();
        }
    }

    fun imGui() {
        components.forEach { it.imGui() }
    }

}