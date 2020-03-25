package com.example.da105_g4_v0.bid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.chat.ChatMessage;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.hanks.htextview.scale.ScaleTextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AuctionDetailsActivity extends AppCompatActivity {

    //    private static final String SERVER_URI = "ws://192.168.43.25:8081/DA105G4_Android_v3.9/MyEchoServer/";
//private static final String SERVER_URI = "ws://192.168.173.3:8081/DA105G4_Android_v3.9/MyEchoServer/";
//    private static final String SERVER_URI = "ws://192.168.196.45:8081/DA105G4_final_0317_0140/MyEchoServer/";
    private static final String SERVER_URI = "ws://da105g4.tk/DA105G4_final_0317_6.30/MyEchoServer/";
    //    private static final String SERVER_URI = "ws://10.0.2.2:8081/DA105G4_Android_v3.9/MyEchoServer/";
    private static String TAG = "AuctionDetailsActivity";
    private MyWebSocketClient myWebSocketClient;
    private TextView tv_aucDetails, tv_price, tv_userBid, tv_curprice_non;
    private Button btSend;
    private ScrollView scrollView, sv_detail;
    private ImageView ivAuc;
    private String userNo;
    private URI uri;
    private String auc_no;
    private Gson gson;
    private OneImageTask getAuctionImageTask, getProfileTask;
    private int bidIncr;
    private int startPrice;
    private long endTimep;
    private long currTimep;
    private String name;
    private ScaleTextView tv_custom;
    private com.hanks.htextview.evaporate.EvaporateTextView tv_curprice;
    private Bitmap profile;
    private LinearLayout layout, exLayout, layout_msg;

    private OneImageTask getOneimg;
private net.cachapa.expandablelayout.ExpandableLayout el, el_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_details);
        findViews();
        Auction auction = (Auction) getIntent().getSerializableExtra("auction");
        auc_no = auction.getAuc_no();
        showDetails(auction);
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        try {
            uri = new URI(SERVER_URI + "/" + memNo + "/" + auction.getAuc_no());
        } catch (URISyntaxException e) {

        }
        String memName = pref.getString("member_name", "");
        name = memName;
        userNo = memNo;

        try {
            uri = new URI(SERVER_URI + "/" + memNo + "/" + auction.getAuc_no());
        } catch (URISyntaxException e) {

        }
        gson = new Gson();

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String endTime = formatter.format(auction.getAuc_endtime());
        long endTimeLong = 0;

        try {
            Date endTimeD = formatter.parse(endTime);
            endTimeLong = endTimeD.getTime();

        } catch (Exception e) {
        }
        endTimep = endTimeLong;


    }

    private void findViews() {
        tv_price = findViewById(R.id.auc_currentPrice);
//        tv_curprice = findViewById(R.id.auc_currentPriceAnime);
        tv_price.setText("目前價錢: ");
//        tvMessage = findViewById(R.id.tv_msg);
        tv_userBid = findViewById(R.id.tv_user_bid);
        btSend = findViewById(R.id.btn_auc_bid);
        layout_msg = findViewById(R.id.linear_exlayout_msg);
        scrollView = findViewById(R.id.sv_msg);
        sv_detail = findViewById(R.id.sv_auction_detail);
        sv_detail.setVerticalScrollBarEnabled(true);
        sv_detail.setNestedScrollingEnabled(true);
        scrollView.setVerticalScrollBarEnabled(true);
//        tvMessage.setMovementMethod(new ScrollingMovementMethod());
        ivAuc = findViewById(R.id.auc_pics);
        tv_aucDetails = findViewById(R.id.auc_details);
        tv_custom = findViewById(R.id.auc_remainingTime);
        layout = findViewById(R.id.linear_chat);
        el = findViewById(R.id.expandable_layout);
        el_msg = findViewById(R.id.expandable_layout_msg);
        exLayout = findViewById(R.id.linear_exlayout);
        tv_curprice_non = findViewById(R.id.auc_currentPriceAnime);
    }

    public void elClick(View view){
        el.toggle();

    }

    public void elClick_msg(View view){
        el_msg.toggle();
    }

    public void showDetails(Auction auction) {
        String url = Util.URL + "/auction/auction.do";
        String url2 = Util.URL + "/member/member.do";
        String auc_no = auction.getAuc_no();
        bidIncr = auction.getAuc_bidincr();
        startPrice = auction.getAuc_startprice();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 6;
        int imageSizex = getResources().getDisplayMetrics().widthPixels / 8;
        Bitmap bitmap = null;
        profile = null;
        SharedPreferences pref = AuctionDetailsActivity.this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mem_no = pref.getString("member_no", "");
        try {
            getAuctionImageTask = new OneImageTask(url, auc_no, imageSize);
            bitmap = getAuctionImageTask.execute().get();

            getProfileTask = new OneImageTask(url2, mem_no, imageSizex);
            profile = getProfileTask.execute().get();
            if (profile != null) {
                profile = getCroppedBitmap(profile);
            } else {
            }

        } catch (Exception e) {

        }

        if (bitmap != null) {
            ivAuc.setImageBitmap(bitmap);
        } else {
            ivAuc.setImageResource(R.drawable.ic_wine_v1n);
        }

        String productInfo = "競標商品名稱: " + auction.getAuc_name() + "\n"
                + "開始金額:$ " + auction.getAuc_startprice() + "\n"
                + "每口價:$ " + auction.getAuc_bidincr() + "\n" + "\n"
                + "商品介紹: " + auction.getAuc_intro() + "\n" + "\n"
                + "廠商介紹: " + auction.getAuc_ven_intro();

        tv_aucDetails.setText(productInfo);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.cart_item:
                Intent intent = new Intent(this, ShoppingCartActivity.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.home_item:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return false;
    }

    private void getCurrentBidMessage() {
        ChatMessage chatMessage = new ChatMessage("currentPrice", "");
        String chatMessageJson = new Gson().toJson(chatMessage);
        myWebSocketClient.send(chatMessageJson);

    }


    private class MyWebSocketClient extends WebSocketClient {


        MyWebSocketClient(URI serverURI) {
            // Draft_17是連接協議，就是標準的RFC 6455（JSR356）
            super(serverURI, new Draft_17());


        }


        @Override
        public void onOpen(ServerHandshake handshakeData) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCurrentBidMessage();
                    changeConnectStatus(true);

                }
            });
            String text = String.format(Locale.getDefault(),
                    "onOpen: Http status code = %d; status message = %s",
                    handshakeData.getHttpStatus(),
                    handshakeData.getHttpStatusMessage());


        }

        @Override
        synchronized public void onMessage(final String message) {

            Log.e("onMessage", "run here");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        ChatMessage chatMessage = gson.fromJson(message, ChatMessage.class);
                        String messageType = chatMessage.getType();

                        if ("currentPrice".equals(messageType)) {
                            Type type = new TypeToken<String>() {
                            }.getType();
                            String bid = new Gson().fromJson(chatMessage.getCurrentBid(), type);
                            int bidIntValue = Integer.valueOf(bid);
                            if (bidIntValue == 0) {
                                tv_curprice_non.setText(String.valueOf(startPrice));

//                                tv_userBid.setText(String.valueOf(startPrice));

                            } else {
//                                tv_userBid.setText(bid);
                                tv_curprice_non.setText(bid);
                            }


                        } else {
                            SharedPreferences pref = AuctionDetailsActivity.this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                            String mem_ac = pref.getString("member_ac", "");
                            String mem_no = pref.getString("member_no", "");
                            String user_ac = jsonObject.get("userName").toString();
                            String message1 = jsonObject.get("message").toString();

//
                            DecimalFormat formatter = new DecimalFormat("#,###");
                            Integer bid = Integer.parseInt(message1);
                            String bidMessage  = formatter.format(bid);

                            if (!(user_ac.equals(mem_ac))) {


                                showMessage(user_ac, bidMessage, true);

                            }
                            else if (user_ac.equals(mem_ac)) {


                                showMessage("", bidMessage, false);

                            }
                            String currBid = message1;
                            tv_curprice_non.setText(currBid);
//                            tv_userBid.setText(currBid);
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        }
                    } catch (JSONException e) {

                    }
                }
            });
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeConnectStatus(false);
                }
            });
            String text = String.format(Locale.getDefault(),
                    "code = %d, reason = %s, remote = %b",
                    code, reason, remote);

        }

        @Override
        public void onError(Exception ex) {

        }


    }

    private void showMessage(String sender, String message, boolean left) {

        String text = sender + " " + message;
        View view;

        // 準備左右2種layout給不同種類發訊者(他人/自己)使用
        if (left) {
            view = View.inflate(this, R.layout.message_right, null);

        } else {
            view = View.inflate(this, R.layout.message_left, null);
            CircleImageView imageView = view.findViewById(R.id.userImg);
            if (profile != null) {
                imageView.setImageBitmap(profile);
            } else {
                imageView.setImageResource(R.drawable.ic_filled_person);
            }
        }
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(text);
        layout.addView(view);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /* 依照連線狀況改變按鈕enable狀態 */
    private void changeConnectStatus(boolean isConnected) {
        if (isConnected) {
            btSend.setEnabled(true);


        } else {
            btSend.setEnabled(false);

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        autoConnect();


        Date date = new Date(System.currentTimeMillis());
        Long currentTimeLong = date.getTime();
        currTimep = currentTimeLong;
        long expiryTime = endTimep - currTimep;
        new CountDownTimer(expiryTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                String time = days + " 天" + " " + hours % 24 + " 小時" + " " + minutes % 60 + " 分" + " " + seconds % 60 + " 秒";
                tv_custom.animateText(time);
            }

            @Override
            public void onFinish() {
                tv_custom.animateText("已結束");
                btSend.setEnabled(false);
            }


        }.start();

    }

    @Override
    public void onResume() {
        super.onResume();

        if (myWebSocketClient == null) {
            autoConnect();
        }


        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bid = tv_curprice_non.getText().toString();
                if(Integer.parseInt(bid)  ==Integer.parseInt(bid) + bidIncr) {
                    return;
                }
                    Integer userBid = Integer.parseInt(bid) + bidIncr;
                    String userBidStr = String.valueOf(userBid);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("roomNO", auc_no);
                    jsonObject.addProperty("userName", userNo);
                    jsonObject.addProperty("message", userBidStr);
                    jsonObject.addProperty("type", "bid");

                    myWebSocketClient.send(jsonObject.toString());


            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        autoDisconnect();


    }

    public void autoConnect() {
        myWebSocketClient = new MyWebSocketClient(uri);
        myWebSocketClient.connect();
    }

    public void autoDisconnect() {
        if (myWebSocketClient != null) {
            myWebSocketClient.close();
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

}
