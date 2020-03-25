package com.example.da105_g4_v0.store;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.member.Member;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.example.da105_g4_v0.ui.StoreFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.da105_g4_v0.main.Util.CART;

public class StoreMerchandiseDetailsActivity extends AppCompatActivity {

    private static String TAG = "MerchandiseDetailsActivity";
    private OneImageTask productImageTask, getMemberProfileTask;
    private CommonTask getMemberEvalTask, getMemberInfo;
    private Product productCart;
    private TextView product_detail, product_rating;
    private RatingBar ratingBar;
    private ListView listView;
    private net.cachapa.expandablelayout.ExpandableLayout el_detail, el_comment;
    private LinearLayout linear_detail, linear_commnet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_merchandise_details);
        Product product = (Product) getIntent().getSerializableExtra("product");

        listView = findViewById(R.id.listView_product);
        el_detail = findViewById(R.id.expandable_layout_detail);
        el_comment = findViewById(R.id.expandable_layout_comment);
        linear_detail = findViewById(R.id.linear_product_detail);
        linear_commnet = findViewById(R.id.linear_commet);
        if (product == null) {
//            Toast.makeText(this, "merchandise not found", Toast.LENGTH_SHORT).show();
        } else {
            showDetails(product);
        }

        getMemberEvaluation(product);
        productCart = product;
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void onCommentClick(View view){

        el_comment.toggle();
    }

    public void onDetailClick(View view){
        el_detail.toggle();
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

    public void showDetails(Product product) {
        ImageView iv = findViewById(R.id.iv_detail);
        String url = Util.URL + "/product/productPhoto.do";
        String prod_no = product.getProd_no();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;
        try {
            productImageTask = new OneImageTask(url, prod_no, imageSize);
            bitmap = productImageTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            iv.setImageResource(R.drawable.default_image);
        }
        product_detail = findViewById(R.id.tv_detail);
        product_rating = findViewById(R.id.product_rating);
        ratingBar = findViewById(R.id.store_ratingBar);
        String productInfo = getString(R.string.wine_name) + product.getProd_name() + "\n"
                + getString(R.string.wine_year) + " " + product.getProd_year() + "\n"
                + getString(R.string.wine_country) + " " + product.getProd_regi() + "\n"
                + getString(R.string.wine_varietal) + " " + product.getProd_var() + "\n"
                + getString(R.string.wine_price) + "$ " + product.getProd_price() + "\n" + "\n"
                + getString(R.string.wine_info) + "\n" + product.getProd_intro();

        String productRating = "(" + product.getVal_count() + ")";
        product_detail.setText(productInfo);
        product_rating.setText(productRating);
        ratingBar.setRating(product.getVal_total());

    }


    public void onAddToCartClicked(View view) {

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialog_pop, null, false);
        new AlertDialog.Builder(StoreMerchandiseDetailsActivity.this)
                .setMessage("加入購物車")
                .setIcon(R.drawable.ic_wine_v1n)
                .setView(view1)
                .show();


        OrderProduct orderProduct = new OrderProduct(productCart, 1);
        int index = CART.indexOf(orderProduct);

        if (index == -1) {
            CART.add(orderProduct);


        } else {
            orderProduct = CART.get(index);
            orderProduct.setQuantity(orderProduct.getQuantity() + 1);
            String str = String.valueOf(orderProduct.getQuantity());
            Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        }
    }

    public void getMemberEvaluation(Product product) {
//        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//        String memNo = pref.getString("member_no", "");
        String url = Util.URL + "/product_eval/product_eval.do";
        if (Util.networkConnected(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
//            jsonObject.addProperty("memNo", memNo);
            jsonObject.addProperty("prodNo", product.getProd_no());

            String jsonOut = jsonObject.toString();
            getMemberEvalTask = new CommonTask(url, jsonOut);
            List<ProductEval> productEvalList = null;
            try {
                String jsonIn = getMemberEvalTask.execute().get();
                Type listType = new TypeToken<List<ProductEval>>() {
                }.getType();
                productEvalList = new Gson().fromJson(jsonIn, listType);


            } catch (Exception e) {
            }

            if (productEvalList == null || productEvalList.isEmpty()) {
//                Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
                return;
            } else {
                listView.setAdapter(new CommentAdapter(this, productEvalList));

            }
        }


    }



    private class CommentAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<ProductEval> productEvalList;
        private int imageSize;

        public CommentAdapter(Context context, List<ProductEval> productEvalList) {
            this.productEvalList = productEvalList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
            // 呼叫getSystemService()方法取得LayoutInflater物件，
            // 可以透過該物件取得指定layout檔案內容後初始化成View物件
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            // 一樣做法
//            layoutInflater = LayoutInflater.from(context);

        }

        private class ViewHolder {
            ImageView iv_memberProfile;
            TextView tv_memberName, tv_commentTime, tv_memberComment;
            RatingBar ratingBar;

        }

        @Override
        // ListView總列數
        public int getCount() {
            return productEvalList.size();
        }


        @Override
        // 回傳該列物件
        public Object getItem(int position) {
            return productEvalList.get(position);
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
                convertView = layoutInflater.inflate(R.layout.card_product_comment, parent, false);
                holder.tv_memberName = convertView.findViewById(R.id.tv_memberName);
                holder.tv_memberComment = convertView.findViewById(R.id.tv_memberComment);
                holder.tv_commentTime = convertView.findViewById(R.id.tv_commentTime);
                holder.iv_memberProfile = convertView.findViewById(R.id.profile_image);
                holder.ratingBar = convertView.findViewById(R.id.member_ratingBar);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String url = Util.URL + "/member/member.do";
            ProductEval productEval = productEvalList.get(position);
            List<String> memberList= new ArrayList<>();

            for(ProductEval pe: productEvalList){
                memberList.add(pe.getMem_no());
            }

            String list = new Gson().toJson(memberList);
                JsonObject jsonObject1 = new JsonObject();
                jsonObject1.addProperty("action", "getAllMembers");
                jsonObject1.addProperty("memberList",list);
                String jsonOut1 = jsonObject1.toString();
                getMemberInfo = new CommonTask(url, jsonOut1);


                List<Member> members = null;

                try {
                    String jsonIn = getMemberInfo.execute().get();
                    Type listType = new TypeToken<List<Member>>() {
                    }.getType();
                    members = new Gson().fromJson(jsonIn, listType);

                } catch (Exception e) {
                    e.toString();
                }
            Member member = members.get(position);


            holder.tv_memberName.setText(member.getMem_name());
            holder.tv_memberComment.setText(productEval.getEval_content());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String commentTime = sdf.format(productEval.getEval_time());
            holder.tv_commentTime.setText(commentTime);
            holder.ratingBar.setRating(productEval.getEval_star());
            String memNo = productEval.getMem_no();
            getMemberProfileTask = new OneImageTask(url, memNo, imageSize, holder.iv_memberProfile);

            getMemberProfileTask.execute();

            return convertView;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (productImageTask != null) {
            productImageTask.cancel(true);
        }

        if (getMemberEvalTask != null) {
            getMemberEvalTask.cancel(true);
        }

        if (getMemberProfileTask != null) {
            getMemberProfileTask.cancel(true);
        }
    }


}
