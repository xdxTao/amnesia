package com.xdx.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 枚举父类
 */
public interface IBaseEnum<K, V, T extends Enum<?>> {
    Map<Class<?>, Map<?, ?>> map = new LinkedHashMap();

    default void initMap(K code, T t) {
        if (map.containsKey(t.getClass())) {
            Map<K, T> tmp = (Map)map.get(t.getClass());
            tmp.put(code, t);
            map.put(t.getClass(), tmp);
        } else {
            Map<K, T> tmp = new LinkedHashMap();
            tmp.put(code, t);
            map.put(t.getClass(), tmp);
        }

    }

    @JsonCreator
    static <T extends Enum<?>, K> T get(Class<T> clazz, K code) {
        if (map.get(clazz) == null) {
            return null;
        } else {
            Object _code = code;
            if (code instanceof String) {
                _code = code.toString().trim();
            }

            return (T) ((Map)map.get(clazz)).get(_code);
        }
    }

    K getCode();

    V getMsg();
}
