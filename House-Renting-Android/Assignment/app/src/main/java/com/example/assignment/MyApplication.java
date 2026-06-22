 package com.example.assignment;
 
 import android.app.Application;
 
 import com.baidu.mapapi.SDKInitializer;
 
 public class MyApplication extends Application {
     @Override
     public void onCreate() {
         super.onCreate();
         // v7.5.0+: 隐私合规授权（必须在 initialize 之前调用）
         SDKInitializer.setAgreePrivacy(this, true);
         SDKInitializer.initialize(this);
     }
 }
