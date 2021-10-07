package com.phr.core

class GameObject (name: String, transform: Transform = Transform(), zIndex: Int = 0) {

    private var name: String = name;
    private var components: MutableList<Component> = ArrayList();
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
}