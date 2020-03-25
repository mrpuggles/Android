package com.example.da105_g4_v0.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;


import com.example.da105_g4_v0.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class OneImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "OneImageTask";
    private String url, keyNo;
    private int imageSize;
    private WeakReference<ImageView> imageViewWeakReference;

    public OneImageTask(String url, String keyNo, int imageSize, ImageView imageView) {
        this.url = url;
        this.keyNo = keyNo;
        this.imageSize = imageSize;
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    public OneImageTask(String url, String keyNo, int imageSize) {
        this.url = url;
        this.keyNo = keyNo;
        this.imageSize = imageSize;

    }


    @Override
    protected Bitmap doInBackground(Object... objects) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getOneImage");
        jsonObject.addProperty("keyNo", keyNo);
        jsonObject.addProperty("imageSize", imageSize);
        return getRemoteImage(url, jsonObject.toString());
    }

    private Bitmap getRemoteImage(String url, String jsonOut) {
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        String base64Str = null;
        String img = null;
        JsonObject jsonIn = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            bw.write(jsonOut);
            bw.close();


            if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                bitmap = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String data;
                StringBuilder sb = new StringBuilder();
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
                base64Str = new Gson().fromJson(sb.toString(), String.class);
                if(base64Str!=null){
                byte[] bytes = Base64.decode(base64Str, Base64.DEFAULT);

                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } else{bitmap =null;}

                bw.close();
            } else {
                Log.d(TAG, "response code: " + connection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();

            }
        }


        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView =null;
        if(imageViewWeakReference!=null) {
            imageView = imageViewWeakReference.get();
        }
       if (isCancelled() || imageView == null) {

            return;
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ic_wine_v1n);
        }
    }


}

