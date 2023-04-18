package com.deador.restshop.factory;

import org.springframework.stereotype.Component;

@Component
public class ObjectFactory<T> {
    public T createObject(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
