/**
 *
 */
package com.bassoon.stockanalyzer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author xxu
 */
public class JsonUtils {
    public static <V> String objectToJson(V v) {
        if (v == null) {
            return "";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(v);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static <V> V jsonToObject(String jsonString, Class<?> collectionClassType, Class<?> elementClassType) {
        if (collectionClassType != null) {
            return jsonToCollection(jsonString, collectionClassType, elementClassType);
        }
        return jsonToBean(jsonString, elementClassType);
    }

    private static <V> V jsonToBean(String jsonString, Class<?> elementClassType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return (V) objectMapper.readValue(jsonString, elementClassType);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static <V> V jsonToCollection(String jsonString, Class<?> collectionClassType, Class<?> elementClassType) {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = getCollectionType(objectMapper, collectionClassType, elementClassType);
        try {
            return objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static JavaType getCollectionType(ObjectMapper objectMapper, Class<?> collectionClass,
                                              Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

}
