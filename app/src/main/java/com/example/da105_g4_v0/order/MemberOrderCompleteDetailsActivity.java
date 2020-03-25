package com.example.da105_g4_v0.order;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.main.Util;

import com.example.da105_g4_v0.store.OrderList;
import com.example.da105_g4_v0.store.OrderMaster;
import com.example.da105_g4_v0.store.Product;
import com.example.da105_g4_v0.store.StoreMerchandiseDetailsActivity;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MemberOrderCompleteDetailsActivity extends AppCompatActivity {
    private static String TAG = "MemberOrderCompleteDetailsActivity";
    private CommonTask getOrderDetailsTask, updateReviewTask, getOneProductTask, getMemberEval;
    private ListView listView;
    private OneImageTask getProPic;
    private EditText getUserInput;
    private String orderNo;
    private int stars;
    private String prodNumber;
    private String productName;
    private RatingBar ratingBarStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order_complete_details);
        listView = findViewById(R.id.listView_orderDetails);


        OrderMaster orderMaster = (OrderMaster) getIntent().getSerializableExtra("order");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getMemberOrder(orderMaster);
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
                return true;

            case R.id.home_item:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                return true;
        }
        return false;
    }

    public void getMemberOrder(OrderMaster orderMaster) {
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        orderNo = orderMaster.getOrder_no();
        String url = Util.URL + "/order/order.do";
        if (Util.networkConnected(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getMemberOrderDetails");
            jsonObject.addProperty("orderNo", orderMaster.getOrder_no());

            String jsonOut = jsonObject.toString();
            getOrderDetailsTask = new CommonTask(url, jsonOut);
            List<OrderList> orderList = null;
            try {
                String jsonIn = getOrderDetailsTask.execute().get();
                Type listType = new TypeToken<List<OrderList>>() {
                }.getType();
                orderList = new Gson().fromJson(jsonIn, listType);


            } catch (Exception e) {
            }

            if (orderList == null || orderList.isEmpty()) {
                Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                listView.setAdapter(new OrderDetailsAdapter(this, orderList));

            }
        }


    }


    private class OrderDetailsAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<OrderList> orderList;

        private int imageSize;

        public OrderDetailsAdapter(Context context, List<OrderList> orderList) {
            this.orderList = orderList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            // 呼叫getSystemService()方法取得LayoutInflater物件，
            // 可以透過該物件取得指定layout檔案內容後初始化成View物件
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            // 一樣做法
//            layoutInflater = LayoutInflater.from(context);

        }

        private class ViewHolder {
            ImageView iv_productPic;
            TextView tv_productName, tv_total, tv_qty;

        }

        @Override
        // ListView總列數
        public int getCount() {
            return orderList.size();
        }


        @Override
        // 回傳該列物件
        public Object getItem(int position) {
            return orderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        // 依照position回傳該列資料所需呈現的UI畫面(View)
        public View getView(int position, View convertView, ViewGroup parent) {
            // 一個convertView就是ListView一列資料的畫面，
            // 因為每一列資料外觀都一樣，只有資料值不同，所以載入相同layout檔案，
            // 第一次還未載入layout，所以必須呼叫layoutInflater.inflate()載入layout檔案並指派給convertView
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.card_member_order_details, parent, false);
                holder.tv_productName = convertView.findViewById(R.id.tv_orderDetails_productName);
                holder.tv_total = convertView.findViewById(R.id.tv_member_orderDetails_total);
                holder.tv_qty = convertView.findViewById(R.id.tv_member_orderDetails_qty);
                holder.iv_productPic = convertView.findViewById(R.id.iv_order_details_comp_pic);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String url = Util.URL + "/product/product.do";
            String url2 = Util.URL + "/product/productPhoto.do";

            final OrderList list = orderList.get(position);
            holder.tv_qty.setText("購買數量: ".concat(String.valueOf(list.getQuantity())));
            holder.tv_total.setText("總金額: ".concat(String.valueOf(list.getSubtotal())));

            String prodNo = list.getProd_no();
            prodNumber = prodNo;
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("action", "getOneProduct");
            jsonObject2.addProperty("prodNo", prodNumber);
            String jsonOut2 = jsonObject2.toString();
            getOneProductTask = new CommonTask(url, jsonOut2);
            Product oneProduct = null;
            try {
                String product = getOneProductTask.execute().get();
                oneProduct = new Gson().fromJson(product, Product.class);

            } catch (Exception e) {
                e.toString();
            }
            prodNo = oneProduct.getProd_no();
            holder.tv_productName.setText("商品名稱: " + oneProduct.getProd_name());
            getProPic = new OneImageTask(url2, prodNo, imageSize, holder.iv_productPic);
            getProPic.execute();

//            Product product = new Product();
//            product.setProd_no(prodNo);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ShowDialog();

                }
            });
            return convertView;
        }


    }


    public void getBar(View view) {
        ratingBarStar = view.findViewById(R.id.rb_review);
        getUserInput = view.findViewById(R.id.et_dialog_review);

    }

    private void ShowDialog() {

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        popDialog.setIcon(R.drawable.ic_wine_v1n);
        popDialog.setTitle("REVIEW");
        View view = inflater.inflate(R.layout.dialog_review, null, false);
        getBar(view);
        popDialog.setView(view);
        popDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        MemberOrderCompleteDetailsActivity.this.ratingBarStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                            @Override
                            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                                stars = (int) (MemberOrderCompleteDetailsActivity.this.ratingBarStar.getRating());
                            }
                        });
                        SharedPreferences pref = MemberOrderCompleteDetailsActivity.this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                        String memNo = pref.getString("member_no", "");
                        if (getUserInput.getText() == null) {
                            Toast.makeText(MemberOrderCompleteDetailsActivity.this, "please Enter", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int starsInt = (int) (MemberOrderCompleteDetailsActivity.this.ratingBarStar.getRating());
                        String input = getUserInput.getText().toString().trim();
                        String url = Util.URL + "/product_eval/product_eval.do";
                        String url2 = Util.URL + "/product/product.do";

                        if (Util.networkConnected(MemberOrderCompleteDetailsActivity.this)) {

                            JsonObject jsonObject3 = new JsonObject();
                            jsonObject3.addProperty("action", "getMemberEval");
                            jsonObject3.addProperty("memNo", memNo);
                            jsonObject3.addProperty("prodNo", prodNumber);
                            jsonObject3.addProperty("orderNo", orderNo);
                            String jsonOut3 = jsonObject3.toString();
                            getMemberEval = new CommonTask(url, jsonOut3);
                            Boolean isEvalExisted = true;
                            try {
                                isEvalExisted = Boolean.valueOf(getMemberEval.execute().get());

                            } catch (Exception e) {
                                e.toString();
                            }

                            if (isEvalExisted == true) {
                                new AlertDialog.Builder(MemberOrderCompleteDetailsActivity.this)
                                        .setMessage("該商品已經評價過了")
                                        .setNeutralButton("Confirm",
                                                new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        dialog.cancel();
                                                    }
                                                }).setCancelable(false).show();

                                return;
                            } else {
                                JsonObject jsonObject1 = new JsonObject();
                                jsonObject1.addProperty("action", "getOneProduct");
                                jsonObject1.addProperty("prodNo", prodNumber);
                                String jsonOut1 = jsonObject1.toString();
                                getOneProductTask = new CommonTask(url2, jsonOut1);
                                Product oneProduct = null;

                                try {
                                    String product = getOneProductTask.execute().get();
                                    oneProduct = new Gson().fromJson(product, Product.class);

                                } catch (Exception e) {
                                    e.toString();
                                }
                                int count = oneProduct.getVal_count();
                                count += 1;
                                int total = oneProduct.getVal_total();
                                if (total == 0) {
                                    total = (total + starsInt);
                                } else {
                                    total = (total + starsInt) / count;
                                }

                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("action", "addReview");
                                jsonObject.addProperty("memNo", memNo);
                                jsonObject.addProperty("input", input);
                                jsonObject.addProperty("stars", starsInt);
                                jsonObject.addProperty("prodNo", prodNumber);
                                jsonObject.addProperty("orderNo", orderNo);
                                jsonObject.addProperty("count", count);
                                jsonObject.addProperty("total", total);
                                String jsonOut = jsonObject.toString();
                                updateReviewTask = new CommonTask(url, jsonOut);
                                updateReviewTask.execute();

                                JsonObject jsonObject2 = new JsonObject();
                                jsonObject2.addProperty("action", "getOneProduct");
                                jsonObject2.addProperty("prodNo", prodNumber);
                                String jsonOut2 = jsonObject2.toString();
                                getOneProductTask = new CommonTask(url2, jsonOut2);
                                oneProduct = null;
                                try {
                                    String product = getOneProductTask.execute().get();
                                    oneProduct = new Gson().fromJson(product, Product.class);

                                } catch (Exception e) {
                                    e.toString();
                                }


                                Intent intent = new Intent(MemberOrderCompleteDetailsActivity.this, StoreMerchandiseDetailsActivity.class);
                                intent.putExtra("product", oneProduct);
                                startActivity(intent);
                                finish();
                            }
                        }

                    }

                })

                // Button Cancel
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        popDialog.create();
        popDialog.show();

    }

    @Override
    public void onStop() {
        super.onStop();

        if (getOrderDetailsTask != null) {
            getOrderDetailsTask.cancel(true);
        }

        if (updateReviewTask != null) {
            updateReviewTask.cancel(true);
        }

        if (getOneProductTask != null) {
            getOneProductTask.cancel(true);
        }

        if (getMemberEval != null) {
            getMemberEval.cancel(true);
        }
    }

}
