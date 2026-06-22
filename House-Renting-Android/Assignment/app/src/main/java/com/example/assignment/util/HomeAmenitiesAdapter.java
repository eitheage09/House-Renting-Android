package com.example.assignment.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeAmenitiesAdapter implements JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            List<String> result = new ArrayList<>();
            for (JsonElement e : json.getAsJsonArray()) {
                result.add(e.getAsString());
            }
            return result;
        }
        String str = json.getAsString();
        if (str != null && !str.isEmpty()) {
            try {
                JsonArray arr = new Gson().fromJson(str, JsonArray.class);
                List<String> result = new ArrayList<>();
                for (JsonElement e : arr) {
                    result.add(e.getAsString());
                }
                return result;
            } catch (Exception ignored) {}
        }
        return new ArrayList<>();
    }
}