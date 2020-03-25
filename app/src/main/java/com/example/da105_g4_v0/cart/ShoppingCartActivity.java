package com.example.da105_g4_v0.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.order.MemberOrderActivity;
import com.example.da105_g4_v0.order.MemberOrderCompleteDetailsActivity;
import com.example.da105_g4_v0.store.OrderMaster;
import com.example.da105_g4_v0.store.OrderProduct;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import static com.example.da105_g4_v0.main.Util.CART;

public class ShoppingCartActivity extends AppCompatActivity {

    private TextView tv_cartTotal;
    private RecyclerView rvCart;
    private Gson gson;
    private CommonTask newOrder;
    private Button btn_checkOut;
    private OneImageTask getProductPhoto;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        tv_cartTotal = findViewById(R.id.tv_cart_total);
        rvCart = findViewById(R.id.rv_cart);
        btn_checkOut = findViewById(R.id.btn_checkOut);
        imageView = findViewById(R.id.iv_empty_cart);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
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


    @Override
    protected void onStart() {
        super.onStart();
        showTotal(CART);
        rvCart.setAdapter(new CartAdapter(this, CART));
        imageView.setImageResource(R.drawable.iconemptycart);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CART.isEmpty()){
                    LayoutInflater inflater = getLayoutInflater();
                    View view = inflater.inflate(R.layout.dialog_pop, null, false);
                    new AlertDialog.Builder(ShoppingCartActivity.this)
                            .setMessage("購物車沒東西喔!!")
                            .setIcon(R.drawable.ic_wine_v1n)
                            .setView(view)
                            .show();

                    return;
                }
                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                String memNo = pref.getString("member_no", "");
                if (Util.networkConnected(ShoppingCartActivity.this)) {
                    String url = Util.URL + "/order/order.do";

                    OrderMaster orderMaster = new OrderMaster();
                    orderMaster.setMem_no(memNo);
                    int sum = 0;
                    for (OrderProduct product : CART) {
                        sum += product.getProd_price() * product.getQuantity();
                    }

                    orderMaster.setOrder_total(sum);
                    orderMaster.setOrderProductList(CART);

                    String order = gson.toJson(orderMaster);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "newOrder");
                    jsonObject.addProperty("memNo", memNo);
                    jsonObject.addProperty("order", order);
                    String jsonOut = jsonObject.toString();
                    newOrder = new CommonTask(url, jsonOut);

                    String result = null;
                    try {
                        result = newOrder.execute().get();

                    } catch (Exception e) {
                        e.toString();
                    }

                    if(result ==null){
//                        Toast.makeText(ShoppingCartActivity.this, "failed", Toast.LENGTH_SHORT).show();

                    }else{

                        CART.clear();
                        showTotal(CART);
                        if (CART.size() <= 0) {
                            rvCart.setVisibility(View.GONE);
                            Intent intent = new Intent(ShoppingCartActivity.this, VisaCard.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("total", String.valueOf(sum));
//                            intent.putExtras(bundle);
                            intent.putExtra("total", String.valueOf(sum));
                            startActivity(intent);
                            finish();
                        } else {
                            rvCart.setVisibility(View.VISIBLE);

                        }


                    }


                }

            }
        });
    }

    private void showTotal(List<OrderProduct> orderProductList) {
        int total = 0;
        for (OrderProduct orderProduct : orderProductList) {
            total += orderProduct.getProd_price() * orderProduct.getQuantity();
        }
        String tv_total = "總金額: " + total;

        tv_cartTotal.setText(tv_total);


    }

    private class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<OrderProduct> orderProductList;
        private int imageSize;

        CartAdapter(Context context, List<OrderProduct> orderProductList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.orderProductList = orderProductList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class CartViewHolder extends RecyclerView.ViewHolder {

            ImageView iv_productPic;
            TextView tv_productName, tv_productPrice, tv_productQty;
            Button btnPlus, btnMinus, btnRemove;

            public CartViewHolder(View view) {
                super(view);
                iv_productPic = view.findViewById(R.id.iv_merchandise_cart);
                tv_productName = view.findViewById(R.id.tv_merchandise_name_cart);
                tv_productPrice = view.findViewById(R.id.tv_merchandise_price_cart);
                tv_productQty = view.findViewById(R.id.tv_merchandise_qty_cart);
                btnPlus = view.findViewById(R.id.btn_plus);
                btnMinus = view.findViewById(R.id.btn_minus);
                btnRemove = view.findViewById(R.id.btn_cartItem_remove);

            }
        }

        @Override
        public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_cart, parent, false);
            CartViewHolder cartViewHolder = new CartViewHolder(view);


            return cartViewHolder;
        }

        @Override
        public void onBindViewHolder(final CartViewHolder cartViewholder, int position) {
            final OrderProduct orderProduct = orderProductList.get(position);
            String prodNo = orderProduct.getProd_no();
            String url = Util.URL + "/product/productPhoto.do";
            getProductPhoto = new OneImageTask(url, prodNo, imageSize, cartViewholder.iv_productPic);
            getProductPhoto.execute();
            cartViewholder.tv_productName.setText(orderProduct.getProd_name());
            cartViewholder.tv_productPrice.setText(String.valueOf(orderProduct.getProd_price()));
            cartViewholder.tv_productQty.setText(String.valueOf(orderProduct.getQuantity()));


            cartViewholder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String qty = cartViewholder.tv_productQty.getText().toString();
                    int addQty = (Integer.parseInt(qty)) + 1;
                    orderProduct.setQuantity(addQty);
                    cartViewholder.tv_productQty.setText(String.valueOf(orderProduct.getQuantity()));
                    showTotal(CART);

                }
            });

            cartViewholder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (orderProduct.getQuantity() == 1) {
                        return;
                    }

                    String qty = cartViewholder.tv_productQty.getText().toString();
                    int minusQty = (Integer.parseInt(qty)) - 1;
                    orderProduct.setQuantity(minusQty);
                    cartViewholder.tv_productQty.setText(String.valueOf(orderProduct.getQuantity()));
                    showTotal(CART);
                }
            });

            cartViewholder.btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CART.remove(orderProduct);
                    showTotal(CART);
                    CartAdapter.this
                            .notifyDataSetChanged();
                }
            });
        }


        @Override
        public int getItemCount() {
        if(CART.isEmpty()){
            imageView.setVisibility(View.VISIBLE);

        }

        else{

            imageView.setVisibility(View.GONE);
        }
            return orderProductList.size();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (newOrder != null) {
            newOrder.cancel(true);
        }
        if (getProductPhoto != null) {
            getProductPhoto.cancel(true);
        }
    }


}
