package com.example.assignment.service;

import android.util.Log;

import com.example.assignment.model.User;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class NotificationService {

    private static final String TAG = "NotificationService";
    private static final String SERVER_KEY = "PUT_YOUR_OWN_KEY_HERE";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    public void sendNotification(String message, String chatRoomID, User user) {
        new Thread(() -> {
            try {
                MediaType mediaType = MediaType.parse("application/json");
                JSONObject jsonNotif = new JSONObject();
                jsonNotif.put("body", message);
                jsonNotif.put("title", user != null ? user.getName() : "Message");
                jsonNotif.put("roomId", chatRoomID);

                JSONObject wholeObj = new JSONObject();
                wholeObj.put("to", user != null ? user.getFcmToken() : "");
                wholeObj.put("notification", jsonNotif);

                RequestBody requestBody = RequestBody.create(wholeObj.toString(), mediaType);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url("https://fcm.googleapis.com/fcm/send")
                        .post(requestBody)
                        .addHeader("Authorization", "key=" + SERVER_KEY)
                        .addHeader("Content-Type", "application/json")
                        .build();

                okhttp3.Response response = client.newCall(request).execute();
                Log.d(TAG, "Notification sent: " + response.code());
            } catch (Exception e) {
                Log.e(TAG, "Error sending notification: " + e.getMessage());
            }
        }).start();
    }
}
