package com.example.assignment.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static ApiClient instance;
    private final OkHttpClient client;
    private final Gson gson;

    private ApiClient() {
        client = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();
        GsonBuilder builder = new GsonBuilder().setLenient();
        builder.registerTypeAdapter(new TypeToken<java.util.List<String>>(){}.getType(),
            (JsonDeserializer<java.util.List<String>>) (json, typeOfT, context) -> {
                if (json.isJsonArray()) {
                    java.util.List<String> list = new java.util.ArrayList<>();
                    for (JsonElement e : json.getAsJsonArray()) {
                        list.add(e.getAsString());
                    }
                    return list;
                }
                return new java.util.ArrayList<>();
            });
        gson = builder.create();
    }

    public Gson getGson() { return gson; }

    public static synchronized ApiClient getInstance() {
        if (instance == null) instance = new ApiClient();
        return instance;
    }

    public <T> T get(String path, Class<T> responseClass) throws IOException {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + path)
                .get()
                .build();
        return execute(request, responseClass);
    }

    public <T> java.util.List<T> getList(String path, Class<T> elementClass) throws IOException {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + path)
                .get()
                .build();
        String json = executeRaw(request);
        Type listType = TypeToken.getParameterized(java.util.List.class, elementClass).getType();
        return gson.fromJson(json, listType);
    }

    public <T, R> R post(String path, T body, Class<R> responseClass) throws IOException {
        String json = gson.toJson(body);
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + path)
                .post(RequestBody.create(json, JSON))
                .build();
        return execute(request, responseClass);
    }

    public <T, R> R put(String path, T body, Class<R> responseClass) throws IOException {
        String json = gson.toJson(body);
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + path)
                .put(RequestBody.create(json, JSON))
                .build();
        return execute(request, responseClass);
    }

    public boolean delete(String path) throws IOException {
        Request request = new Request.Builder()
                .url(ApiConfig.BASE_URL + path)
                .delete()
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    private <T> T execute(Request request, Class<T> responseClass) throws IOException {
        String json = executeRaw(request);
        return gson.fromJson(json, responseClass);
    }

    private String executeRaw(Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response.code() + ": " + response.body().string());
            }
            String body = response.body() != null ? response.body().string() : "";
            return body;
        }
    }
}