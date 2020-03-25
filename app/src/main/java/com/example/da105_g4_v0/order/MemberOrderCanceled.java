package com.example.da105_g4_v0.order;

import android.content.Context;
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
import android.widget.Toast;

import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.store.OrderMaster;
import com.example.da105_g4_v0.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MemberOrderCanceled extends Fragment {


    public static final String CANCELED_TAG = "MemberOrderCanceled";
    private RecyclerView recyclerView;
    private CommonTask getOrderList;
    private Gson gson;
    private OrderCancelAdapter adapter;
    private String memNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        memNumber = memNo;
        View rootView = inflater.inflate(R.layout.fragment_member_order_canceled, container, false);

        recyclerView = rootView.findViewById(R.id.rv_orderCancel);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(Util.networkConnected(this.getActivity())) {

            updateUI(memNumber);
        }
    }
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//
//                updateUI(memNumber);
//
//        }
//    }


    public void updateUI(String memNo) {
        String url = Util.URL + "/order/order.do";


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getOrderCanceled");
        jsonObject.addProperty("memNo", memNo);

        String jsonOut = jsonObject.toString();


        getOrderList = new CommonTask(url, jsonOut);
        List<OrderMaster> orderMasterList = null;

        try {
            String jsonIn = getOrderList.execute().get();
            if(jsonIn==null){
                return;
            }
            Type listType = new TypeToken<List<OrderMaster>>() {
            }.getType();
            orderMasterList = gson.fromJson(jsonIn, listType);


        } catch (Exception e) {
            Log.e(CANCELED_TAG, e.toString());
        }

        //list有資料進來直接去else, 如果list沒有資料進來,把現在存在list裡面的物件給移除
        if (orderMasterList == null || orderMasterList.isEmpty()) {

                try {
                    adapter.clearData();
                } catch (Exception e) {
                }



        } else {
            if (adapter == null) {
                adapter = new OrderCancelAdapter(MemberOrderCanceled.this.getActivity(), orderMasterList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(orderMasterList);
                recyclerView.setAdapter(adapter);
            }

        }

    }


    private class OrderCancelAdapter extends RecyclerView.Adapter<MemberOrderCanceled.OrderCancelAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<OrderMaster> orderMasterList;
        private int imageSize;


        public OrderCancelAdapter(Context context, List<OrderMaster> orderMasterList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.orderMasterList = orderMasterList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_orderNo, tv_orderTime, tv_orderTotal;
            ImageView iv_productPic;


            public ViewHolder(View view) {
                super(view);
                tv_orderNo = view.findViewById(R.id.tv_orderNo_title_order_canceled);
                tv_orderTime = view.findViewById(R.id.tv_orderTime_order_canceled);
                tv_orderTotal = view.findViewById(R.id.tv_orderTotal_order_canceled);


                iv_productPic = view.findViewById(R.id.iv_merchandise_order_canceled);


            }
        }


        @Override
        public MemberOrderCanceled.OrderCancelAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_order_canceled, parent, false);
            MemberOrderCanceled.OrderCancelAdapter.ViewHolder viewHolder = new MemberOrderCanceled.OrderCancelAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MemberOrderCanceled.OrderCancelAdapter.ViewHolder viewholder, int position) {
            final OrderMaster orderMaster = orderMasterList.get(position);

            String order_no = orderMaster.getOrder_no();
            String order_total = String.valueOf(orderMaster.getOrder_total());
            String order_time = String.valueOf(orderMaster.getOrder_time());

            viewholder.tv_orderNo.setText(order_no);
            viewholder.tv_orderTime.setText(order_time);
            viewholder.tv_orderTotal.setText(order_total);


        }

        @Override
        public int getItemCount() {

            return orderMasterList.size();
        }

        public void setData(List<OrderMaster> orderMasterList) {
           this.orderMasterList =orderMasterList ;
            notifyDataSetChanged();
        }

        public void clearData() {
            if(this.orderMasterList.size() >0)
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

    }

}
