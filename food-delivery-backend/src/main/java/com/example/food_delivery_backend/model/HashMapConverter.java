package com.example.food_delivery_backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = false)
public class HashMapConverter implements AttributeConverter<Map<Long, Integer>, String> {

    @Override
    public String convertToDatabaseColumn(Map<Long, Integer> map) {
        if (map == null || map.isEmpty()) return "{}";

        StringBuilder sb = new StringBuilder("{");

        for (Map.Entry<Long, Integer> entry : map.entrySet()) {
            sb.append("\"")                 // key must be string
                    .append(entry.getKey())
                    .append("\":")
                    .append(entry.getValue())
                    .append(",");
        }

        // remove last comma
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");

        return sb.toString();               // valid JSON
    }

    @Override
    public Map<Long, Integer> convertToEntityAttribute(String json) {
        Map<Long, Integer> map = new HashMap<>();

        if (json == null || json.trim().isEmpty() || json.equals("{}"))
            return map;

        // Remove { }
        json = json.trim();
        json = json.substring(1, json.length() - 1);

        String[] pairs = json.split(",");

        for (String pair : pairs) {
            String[] kv = pair.split(":");

            String keyStr = kv[0].replace("\"", "").trim();
            String valueStr = kv[1].trim();

            Long key = Long.parseLong(keyStr);
            Integer value = Integer.parseInt(valueStr);

            map.put(key, value);
        }

        return map;
    }
}
