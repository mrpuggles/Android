package com.example.da105_g4_v0.main;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.da105_g4_v0.store.OrderProduct;
import com.example.da105_g4_v0.ws.ChatWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Util {

//    public static String URL = "http://192.168.196.45:8081/DA105G4_final_0317_0140";
    //手機
//    public static String URL ="http://192.168.43.25:8081/DA105G4_Android_v3.9";
    //家裡
//    public static String URL = "http://192.168.173.3:8081/DA105G4_Android_v3.9";
//    public static String URL = "http://192.168.137.1:8081/DA105G4_final_0316_00.15";
    public static String URL = "http://da105g4.tk/DA105G4_final_0317_6.30";

    public final static String PREF_FILE = "preference";
    public static List<OrderProduct> CART = new ArrayList<>();


    public static final String SERVER_URI =
            "ws://10.0.2.2:8081/DA105G439/MyEchoServer/";

//    public static final String SERVER_URI =
//            "ws://192.168.43.25:8081/DA105G4_Android_v3.9/MyEchoServer/";

    public static ChatWebSocketClient chatWebSocketClient;

    // 建立WebSocket連線
    public static void connectServer(Context context, String userName) {
        URI uri = null;
        try {
            uri = new URI(SERVER_URI + userName);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        if (chatWebSocketClient == null) {
            chatWebSocketClient = new ChatWebSocketClient(uri, context);
            chatWebSocketClient.connect();
        }
    }

    // 中斷WebSocket連線
    public static void disconnectServer() {
        if (chatWebSocketClient != null) {
            chatWebSocketClient.close();
            chatWebSocketClient = null;
        }
    }

    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager != null ? conManager.getActiveNetworkInfo() : null;
        return networkInfo != null && networkInfo.isConnected();

    }

    public static void showToast(Context context, int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
    }

    public static int getImageScale(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int scale = 1;
        while (options.outWidth / scale >= width || options.outHeight / scale >= height) {
            scale *= 2;
        }

        return scale;
    }

}
