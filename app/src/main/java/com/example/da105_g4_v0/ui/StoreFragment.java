package com.example.da105_g4_v0.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.chat.ChatMessage;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.store.Product;
import com.example.da105_g4_v0.store.StoreMerchandiseDetailsActivity;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public class StoreFragment extends Fragment {
    private static final String TAG = "StoreFragment";
    //    private Spinner spYear, spRegions, spVarietals;
    private CommonTask getProductTask, getCompositeQueryTask;
    private OneImageTask productImageTask;
    private RecyclerView recyclerView;
    private int shortAnimationDuration;
    private com.skydoves.powerspinner.PowerSpinnerView spYear, spRegions, spVarietals;
    private net.cachapa.expandablelayout.ExpandableLayout expand;
    private IconSpinnerItem itYear, itRegions, itVarietals;
    List<IconSpinnerItem> yearSpinnerItems = new ArrayList<>();
    List<IconSpinnerItem> regiSpinnerItems = new ArrayList<>();
    List<IconSpinnerItem> varSpinnerItems = new ArrayList<>();
    private String strYear = "";
    private String strVar ="";
    private String strRegi ="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        expand = rootView.findViewById(R.id.expandable_layout);


        setHasOptionsMenu(true);
        recyclerView = rootView.findViewById(R.id.rv_store);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        spYear = rootView.findViewById(R.id.sp_filter_year);
        spRegions = rootView.findViewById(R.id.sp_filter_regions);
        spVarietals = rootView.findViewById(R.id.sp_filter_varietals);

        Toolbar toolbar = rootView.findViewById(R.id.tb_cart);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tb_store, menu);
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

            case R.id.filter_item:
                expand.toggle();

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
        } else {
            Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onResume() {
        super.onResume();


    }



    private void updateUI(String jsonOut) {
        getProductTask = new CommonTask(Util.URL + "/product/product.do", jsonOut);
        List<Product> productList = null;

        try {
            String jsonIn = getProductTask.execute().get();
            Type listType = new TypeToken<List<Product>>() {
            }.getType();
            productList = new Gson().fromJson(jsonIn, listType);

            Set<String> searchYear = new LinkedHashSet<>();
            Set<String> searchRegions = new LinkedHashSet<>();
            Set<String> searchVarietals = new LinkedHashSet<>();
            for (int i = 0; i < productList.size(); i++) {
                searchYear.add(String.valueOf(productList.get(i).getProd_year()));
                searchRegions.add(productList.get(i).getProd_regi());
                searchVarietals.add(productList.get(i).getProd_var());
            }
            List<String> searchYearList = new ArrayList<>();
            List<String> searchRegionsList = new ArrayList<>();
            List<String> searchVarietalsList = new ArrayList<>();

            for (String year : searchYear) {
                searchYearList.add(year);
            }

            for (String regi : searchRegions) {
                searchRegionsList.add(regi);
            }

            for (String var : searchVarietals) {
                searchVarietalsList.add(var);
            }

            Collections.sort(searchYearList);
            Collections.sort(searchRegionsList);
            Collections.sort(searchVarietalsList);
            searchYearList.add("不拘");
            searchRegionsList.add("不拘");
            searchVarietalsList.add("不拘");
            spinnerFilter(searchYearList, searchRegionsList, searchVarietalsList);


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (productList == null || productList.isEmpty()) {

        } else {
            recyclerView.setAdapter(new StoreAdapter(getActivity(), productList));
        }
    }

    private void spinnerUpdate(String jsonOut) {
        getCompositeQueryTask = new CommonTask(Util.URL + "/product/product.do", jsonOut);
        List<Product> productList = null;


        try {
            String jsonIn = getCompositeQueryTask.execute().get();
            Type listType = new TypeToken<List<Product>>() {
            }.getType();
            productList = new Gson().fromJson(jsonIn, listType);


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (productList == null || productList.isEmpty()) {
//            Toast.makeText(getActivity(), "product not found", Toast.LENGTH_SHORT).show();

            return;
        } else {
            recyclerView.setAdapter(new StoreAdapter(getActivity(), productList));
        }
    }

    public void spinnerFilter(List<String> spinnerYear, List<String> spinnerRegions, List<String> spinnerVarietals) {


       if(!yearSpinnerItems.isEmpty()){

           yearSpinnerItems.clear();
       }

        if(!regiSpinnerItems.isEmpty()){

            regiSpinnerItems.clear();
        }

        if(!varSpinnerItems.isEmpty()){

            varSpinnerItems.clear();
        }

        Bitmap bitMap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_outlined_singed_in);



        Drawable d = new BitmapDrawable(getResources(), bitMap);
        for (String val : spinnerYear) {
            yearSpinnerItems.add(new IconSpinnerItem(d ,val));
        }
        for (String val : spinnerRegions) {
            regiSpinnerItems.add(new IconSpinnerItem(d, val));
        }

        for (String val : spinnerVarietals) {
            varSpinnerItems.add(new IconSpinnerItem(d, val));
        }

        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(spYear);
        spYear.setSpinnerAdapter(iconSpinnerAdapter);
        spYear.setItems(yearSpinnerItems);
        spYear.selectItemByIndex(yearSpinnerItems.size() - 1);

        IconSpinnerAdapter iconSpinnerAdapter2 = new IconSpinnerAdapter(spRegions);
        spRegions.setSpinnerAdapter(iconSpinnerAdapter2);
        spRegions.setItems(regiSpinnerItems);
        spRegions.selectItemByIndex(regiSpinnerItems.size() - 1);

        IconSpinnerAdapter iconSpinnerAdapter3 = new IconSpinnerAdapter(spVarietals);
        spVarietals.setSpinnerAdapter(iconSpinnerAdapter3);
        spVarietals.setItems(varSpinnerItems);
        spVarietals.selectItemByIndex(varSpinnerItems.size() - 1);


//        spinnerView.setLifecycleOwner(this);

        spYear.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                itYear = yearSpinnerItems.get(i);

                strYear = itYear.component2().trim();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getCompositeQuery");
                jsonObject.addProperty("prod_year", strYear);
                jsonObject.addProperty("prod_regi", strRegi);
                jsonObject.addProperty("prod_var", strVar);
                String jsonOutYear = jsonObject.toString();

                spinnerUpdate(jsonOutYear);

            }


        });


//        ArrayAdapter<String> adapter = new ArrayAdapter(getActivity(),
//                android.R.layout.simple_list_item_1, spinnerYear);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spYear.setAdapter(adapter);
//        spYear.setSelection(spinnerYear.size() - 1);
//        spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                String strYear = spYear.getSelectedItem().toString().trim();
//                String strRegi = spRegions.getSelectedItem().toString().trim();
//                String strVar = spVarietals.getSelectedItem().toString().trim();
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "getCompositeQuery");
//                jsonObject.addProperty("prod_year", strYear);
//                jsonObject.addProperty("prod_regi", strRegi);
//                jsonObject.addProperty("prod_var", strVar);
//                String jsonOutYear = jsonObject.toString();
//
//                spinnerUpdate(jsonOutYear);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        spinnerView.setLifecycleOwner(this);

        spRegions.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                itRegions = regiSpinnerItems.get(i);

                strRegi = itRegions.component2().trim();


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getCompositeQuery");
                jsonObject.addProperty("prod_year", strYear);
                jsonObject.addProperty("prod_regi", strRegi);
                jsonObject.addProperty("prod_var", strVar);
                String jsonOutYear = jsonObject.toString();

                spinnerUpdate(jsonOutYear);

            }


        });


//        ArrayAdapter<String> adapter2 = new ArrayAdapter(getActivity(),
//                android.R.layout.simple_list_item_1, spinnerRegions);
//        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spRegions.setAdapter(adapter2);
//        spRegions.setSelection(spinnerRegions.size() - 1);
//        spRegions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String strYear = spYear.getSelectedItem().toString().trim();
//                String strRegi = spRegions.getSelectedItem().toString().trim();
//                String strVar = spVarietals.getSelectedItem().toString().trim();
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "getCompositeQuery");
//
//                jsonObject.addProperty("prod_year", strYear);
//                jsonObject.addProperty("prod_regi", strRegi);
//                jsonObject.addProperty("prod_var", strVar);
//                String jsonOutRegi = jsonObject.toString();
//
//
//                spinnerUpdate(jsonOutRegi);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });


//        spinnerView.setLifecycleOwner(this);

        spVarietals.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                itVarietals = varSpinnerItems.get(i);

                strVar = itVarietals.component2().trim();

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getCompositeQuery");
                jsonObject.addProperty("prod_year", strYear);
                jsonObject.addProperty("prod_regi", strRegi);
                jsonObject.addProperty("prod_var", strVar);
                String jsonOutYear = jsonObject.toString();

                spinnerUpdate(jsonOutYear);

            }


        });


//        ArrayAdapter<String> adapter3 = new ArrayAdapter(getActivity(),
//                android.R.layout.simple_list_item_1, spinnerVarietals);
//        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spVarietals.setAdapter(adapter3);
//        spVarietals.setSelection(spinnerVarietals.size() - 1);
//        spVarietals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String strYear = spYear.getSelectedItem().toString().trim();
//                String strRegi = spRegions.getSelectedItem().toString().trim();
//                String strVar = spVarietals.getSelectedItem().toString().trim();
//
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "getCompositeQuery");
//                jsonObject.addProperty("prod_year", strYear);
//                jsonObject.addProperty("prod_regi", strRegi);
//                jsonObject.addProperty("prod_var", strVar);
//                String jsonOutVar = jsonObject.toString();
//
//                spinnerUpdate(jsonOutVar);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

    }


    private class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<Product> productList;

        private int imageSize;


        public StoreAdapter(Context context, List<Product> productList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.productList = productList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_productName, tv_productPrice, tv_productVar, tv_productCountry, tv_productYear;
            ImageView iv_productPic;
            ProgressBar loadingView;
            RatingBar rating;

            public ViewHolder(View view) {
                super(view);
                tv_productName = view.findViewById(R.id.tv_merchandise_name);
                tv_productPrice = view.findViewById(R.id.tv_merchandise_price);
                tv_productVar = view.findViewById(R.id.tv_merchandise_varietal);
                tv_productCountry = view.findViewById(R.id.tv_auction_currentPrice);
                tv_productYear = view.findViewById(R.id.tv_merchandise_year);

                iv_productPic = view.findViewById(R.id.iv_merchandise);

                loadingView = view.findViewById(R.id.loading_spinner);

                rating = view.findViewById(R.id.store_ratingBar);

            }
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_main_store, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewholder, int position) {
            final Product product = productList.get(position);
            viewholder.loadingView.setVisibility(View.VISIBLE);
            int count = 0;
            String url = Util.URL + "/product/productPhoto.do";
            String product_no = product.getProd_no();

            shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

            productImageTask = new OneImageTask(url, product_no, imageSize, viewholder.iv_productPic);

            productImageTask.execute();
            count++;
            if (count > 0) {
                viewholder.loadingView.animate().alpha(0f).setDuration(shortAnimationDuration).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        viewholder.loadingView.setVisibility(View.GONE);
                    }
                });
            }

            count = 0;

            viewholder.rating.setRating(product.getVal_total());
            viewholder.tv_productName.setText(product.getProd_name());
            viewholder.tv_productYear.setText(String.valueOf(product.getProd_year()));
            viewholder.tv_productCountry.setText(product.getProd_regi());
            viewholder.tv_productVar.setText(product.getProd_var());
            viewholder.tv_productPrice.setText("$ ".concat(String.valueOf(product.getProd_price())));

            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),
                            StoreMerchandiseDetailsActivity.class);
                    intent.putExtra("product", product);
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return productList.size();
        }


    }




    @Override
    public void onStop() {
        super.onStop();
        if (getProductTask != null) {
            getProductTask.cancel(true);
        }

        if (getCompositeQueryTask != null) {
            getCompositeQueryTask.cancel(true);
        }

        if (productImageTask != null) {
            productImageTask.cancel(true);
        }

    }
}
