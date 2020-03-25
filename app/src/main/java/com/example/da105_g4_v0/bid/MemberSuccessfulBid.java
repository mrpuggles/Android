package com.example.da105_g4_v0.bid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.store.StoreMerchandiseDetailsActivity;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MemberSuccessfulBid extends Fragment {

    public static final String TAG = "MemberSuccessfulBid";
    private RecyclerView recyclerView;
    private CommonTask getOrderList;
    private OneImageTask  getProductPic;
    private Gson gson;
    private OrderAdapter adapter;
    private String memNumber;
    private String orderNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        SharedPreferences pref = this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        memNumber = memNo;
        View rootView = inflater.inflate(R.layout.fragment_member_successful_bid, container, false);
        recyclerView = rootView.findViewById(R.id.rv_memberSuccessfulBid);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.networkConnected(this.getActivity())) {

            updateUI(memNumber);
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//
//            updateUI(memNumber);
//
//        }
//    }


    public void updateUI(String memNo) {

        String url = Util.URL+"/auction_order/auction_order.do";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getSuccessfulBid");
        jsonObject.addProperty("memNo", memNo);

        String jsonOut = jsonObject.toString();

        getOrderList = new CommonTask(url, jsonOut);
        List<AuctionOrder> orderMasterList = null;

        try {
            String jsonIn = getOrderList.execute().get();
            if(jsonIn ==null){
                return;
            }
            Type listType = new TypeToken<List<AuctionOrder>>() {
            }.getType();
            orderMasterList = gson.fromJson(jsonIn, listType);


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        if (orderMasterList == null || orderMasterList.isEmpty()) {

            try {
                adapter.clearData();
            } catch (Exception e) {
            }


        } else {
            if (adapter == null) {
                adapter = new OrderAdapter(MemberSuccessfulBid.this.getActivity(), orderMasterList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(orderMasterList);
                recyclerView.setAdapter(adapter);
            }


        }
    }


    private class OrderAdapter extends RecyclerView.Adapter<MemberSuccessfulBid.OrderAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<AuctionOrder> orderMasterList;
        private int imageSize;


        public OrderAdapter(Context context, List<AuctionOrder> orderMasterList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.orderMasterList = orderMasterList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_orderNo, tv_orderTotal;
            ImageView iv_productPic;


            public ViewHolder(View view) {
                super(view);
                tv_orderNo = view.findViewById(R.id.tv_auctionNo_order_complete);
                tv_orderTotal = view.findViewById(R.id.tv_auctionTotal_order_complete);
                iv_productPic = view.findViewById(R.id.iv_auction_order);


            }
        }


        @Override
        public MemberSuccessfulBid.OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_bid_successful, parent, false);
            MemberSuccessfulBid.OrderAdapter.ViewHolder viewHolder = new MemberSuccessfulBid.OrderAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MemberSuccessfulBid.OrderAdapter.ViewHolder viewHolder, final int position) {
            final AuctionOrder orderMaster = orderMasterList.get(position);
            String url = Util.URL + "/auction/auction.do";
            String orderNo = orderMaster.getAuc_ord_no();
            orderNumber= orderNo;
            String order = orderMaster.getAuc_no();
            String bidPrice = String.valueOf(orderMaster.getAuc_ord_price());
            viewHolder.tv_orderNo.setText(orderNo);
            viewHolder.tv_orderTotal.setText(bidPrice);
            getProductPic = new OneImageTask(url, order, imageSize, viewHolder.iv_productPic);
            getProductPic.execute();

//            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getActivity(),
//                            StoreMerchandiseDetailsActivity.class);
//                    intent.putExtra("bid",   orderNumber);
//                    startActivity(intent);
//
//                }
//            });

        }

        @Override
        public int getItemCount() {

            return orderMasterList.size();
        }

        public void setData(List<AuctionOrder> orderMasterList) {
            this.orderMasterList = orderMasterList;
            notifyDataSetChanged();
        }

        public void clearData() {
            if (this.orderMasterList.size() > 0)
                this.orderMasterList.clear();
            notifyDataSetChanged();
        }
    }


    @Override
    public void onStop() {
        super.onStop();

        if (getOrderList != null) {
            getOrderList.cancel(true);
        }

        if (getProductPic != null) {
            getProductPic.cancel(true);
        }


    }


}
