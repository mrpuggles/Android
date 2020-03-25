package com.example.da105_g4_v0.ui;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.CountDownTimer;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.bid.Auction;
import com.example.da105_g4_v0.bid.AuctionDetailsActivity;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;

import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;

import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CommonTask getAuctionItemsTask, getHighestBidTask;
    private ImageTask getAuctionImageTask;
    private Gson gson;
    private long endTimep;
    private long currTimep;
    private AuctionAdapter adapter;

    private Toolbar toolbar;
    private long expTime;
    private Integer highestBidInt, startPriceInt;
    private String timeStr;
    private List<Integer> inte;
    private OneImageTask getOneimg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        recyclerView = rootView.findViewById(R.id.rv_auction);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar = rootView.findViewById(R.id.tb_cart);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.cart_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.cart_item:
                Intent intent = new Intent(getActivity(), ShoppingCartActivity.class);
                startActivity(intent);

                return true;

            case R.id.home_item:
                Intent intent2 = new Intent(getActivity(), MainActivity.class);
                startActivity(intent2);

                return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(getActivity())) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");

            String jsonOut = jsonObject.toString();


            updateUI(jsonOut);

        }


    }

    private void updateUI(String jsonOut) {
        getAuctionItemsTask = new CommonTask(Util.URL + "/auction/auction.do", jsonOut);

        List<Auction> auctionList = null;

        try {
            String jsonIn = getAuctionItemsTask.execute().get();
            Type listType = new TypeToken<List<Auction>>() {
            }.getType();
            auctionList = gson.fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        if (auctionList == null || auctionList.isEmpty()) {


        } else {

            recyclerView.setAdapter(new AuctionAdapter(getActivity(), auctionList));

            List<String> str = new ArrayList<>();
            inte = new ArrayList<>();
            for (Auction ac : auctionList) {
                str.add(ac.getAuc_no());
            }

            for (String str1 : str) {
                String url = Util.URL + "/auction_order/auction_order.do";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getHighestBid");
                jsonObject.addProperty("auc_no", str1);
                String jsonOut1 = jsonObject.toString();

                getHighestBidTask = new CommonTask(url, jsonOut1);

                int highestBid = 0;
                try {

                    Type type = new TypeToken<Integer>() {
                    }.getType();
                    String jsonIn = getHighestBidTask.execute().get();
                    highestBid = gson.fromJson(jsonIn, type);

                    inte.add(highestBid);


                } catch (
                        Exception e) {
                }

            }
        }

    }


    private class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        public List<Auction> auctionList;
        private int imageSize;


        public AuctionAdapter(Context context, List<Auction> auctionList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.auctionList = auctionList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 10;

        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_auctionName, tv_countDown, tv_auctionPrice;
            ImageView iv_auctionPic;
            ProgressBar loadingView;
            CountDownTimer timer;


            public ViewHolder(View view) {
                super(view);
                tv_auctionName = view.findViewById(R.id.tv_auction_name);
                tv_countDown = view.findViewById(R.id.tv_auction_countDown);
                iv_auctionPic = view.findViewById(R.id.iv_auction);
                tv_auctionPrice = view.findViewById(R.id.tv_auction_currentPrice_card);


            }


        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_main_auction, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            if(viewHolder.timer !=null){
                viewHolder.timer.cancel();

            }
            final Auction auction = auctionList.get(position);
            final Integer currentPriceInt = inte.get(position);
            String url2 = Util.URL + "/auction/auction.do";
            String auc_no = auction.getAuc_no();
            int startPrice = auction.getAuc_startprice();
            startPriceInt = startPrice;


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeFragment.this.getActivity(), AuctionDetailsActivity.class);
                    intent.putExtra("auction", auction);
                    startActivity(intent);
                }
            });


            getOneimg = new OneImageTask (url2, auc_no, imageSize, viewHolder.iv_auctionPic);

            try {
                getOneimg.execute();
            } catch (Exception e) {
            }


            viewHolder.tv_auctionName.setText(auction.getAuc_name());


            if (currentPriceInt == 0) {
                viewHolder.tv_auctionPrice.setText(String.valueOf(startPriceInt));
            } else {
                viewHolder.tv_auctionPrice.setText(String.valueOf(currentPriceInt));
            }

            endTimep = 0;
            currTimep = 0;

            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String endTime = formatter.format(auction.getAuc_endtime());
            long endTimeLong = 0;

            try {
                Date endTimeD = formatter.parse(endTime);
                endTimeLong = endTimeD.getTime();

            } catch (Exception e) {
            }

            Date date = new Date(System.currentTimeMillis());
            Long currentTimeLong = date.getTime();

            long expiryTime = endTimeLong - currentTimeLong;

            viewHolder.timer = new CountDownTimer(expiryTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    String time = days + " 天" + " " + hours % 24 + " 小時" + " " + minutes % 60 + " 分" + " " + seconds % 60 + " 秒";
                    viewHolder.tv_countDown.setText(time);
                }

                @Override
                public void onFinish() {
                    viewHolder.tv_countDown.setText("已結束");

                }


            }.start();
//            int time = (int) expiryTime / 1000;
//            int day = time / 86400;
//
//            int hour = (time / 3600) - (day * 24);
//
//            int min = (time / 60) - ((day * 24 * 60) + (hour * 60));
//
//            int sec = time - ((day * 24 * 60 * 60) + (hour * 60 * 60) + (min * 60));
//
//            String showTime = day + "天" + hour + "小時" + min + "分" + sec + "秒";
//
//
//
//           auction.setShowTime(showTime);
//
//
//            if (expiryTime >= 0) {
//                viewHolder.tv_countDown.setText(auction.getShowTime());
//            }

        }


        @Override
        public int getItemCount() {

            return auctionList.size();
        }

        public void setData(List<Auction> auctionList) {
            this.auctionList = auctionList;
            notifyDataSetChanged();
        }

        public void clearData() {
            if (this.auctionList.size() > 0)
                this.auctionList.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getAuctionItemsTask != null) {
            getAuctionItemsTask.cancel(true);
        }

        if (getAuctionImageTask != null) {
            getAuctionImageTask.cancel(true);
        }
        if (getHighestBidTask != null) {
            getHighestBidTask.cancel(true);
        }


    }

}

