 package com.example.assignment.util;
 
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.drawable.BitmapDrawable;
 import android.widget.ImageView;
 
 import java.io.ByteArrayOutputStream;
 
 public class ImageUtils {
 
     public static Bitmap crop(Bitmap source, int width, int height) {
         int sw = source.getWidth();
         int sh = source.getHeight();
         double sratio = 1.0 * sw / sh;
 
         int x, y, w, h;
         double ratio = 1.0 * width / height;
 
         if (ratio >= sratio) {
             w = sw;
             h = (int) (sw / ratio);
             x = 0;
             y = (sh - h) / 2;
         } else {
             w = (int) (sh * ratio);
             h = sh;
             x = (sw - w) / 2;
             y = 0;
         }
 
         return Bitmap.createScaledBitmap(Bitmap.createBitmap(source, x, y, w, h), width, height, true);
     }
 
     public static byte[] bitmapToBytes(Bitmap bitmap) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 80, baos);
         return baos.toByteArray();
     }
 
     public static Bitmap bytesToBitmap(byte[] bytes) {
         if (bytes == null || bytes.length == 0) return null;
         return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
     }
 
     public static byte[] imageViewToBytes(ImageView imageView, int width, int height) {
         Bitmap bitmap = null;
         if (imageView.getDrawable() instanceof BitmapDrawable) {
             bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
         }
         if (bitmap == null) return new byte[0];
         return bitmapToBytes(crop(bitmap, width, height));
     }
 
    public static void setImageBytes(ImageView imageView, byte[] bytes) {
        Bitmap bitmap = bytesToBitmap(bytes);
        if (bitmap != null) imageView.setImageBitmap(bitmap);
    }
 
     public static java.util.List<byte[]> parsePhotoJson(String photoJson) {
         java.util.List<byte[]> result = new java.util.ArrayList<>();
         if (photoJson == null || photoJson.isEmpty() || photoJson.equals("[]")) return result;
         try {
             com.google.gson.reflect.TypeToken<java.util.List<String>> token = new com.google.gson.reflect.TypeToken<java.util.List<String>>(){};
             java.util.List<String> base64List = new com.google.gson.Gson().fromJson(photoJson, token.getType());
             for (String b64 : base64List) result.add(android.util.Base64.decode(b64, android.util.Base64.DEFAULT));
         } catch (Exception e) { e.printStackTrace(); }
         return result;
     }
}
