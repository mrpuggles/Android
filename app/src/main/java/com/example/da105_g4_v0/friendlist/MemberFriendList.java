package com.example.da105_g4_v0.friendlist;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.da105_g4_v0.R;

import com.skydoves.powerspinner.IconSpinnerAdapter;
import com.skydoves.powerspinner.IconSpinnerItem;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;


import java.util.ArrayList;
import java.util.List;


public class MemberFriendList extends AppCompatActivity {
    List<IconSpinnerItem> iconSpinnerItems = new ArrayList<>();
    private PowerSpinnerView spinnerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_friend_list);

        spinnerView = findViewById(R.id.pwsp);

        List<String> text = new ArrayList<>();
        text.add("test1");
        text.add("test2");
        text.add("test3");



        for(String val : text) {
            iconSpinnerItems.add(new IconSpinnerItem(Drawable.createFromPath("M10,20v-6h4v6h5v-8h3L12,3 2,12h3v8z"), val));
        }


        IconSpinnerAdapter iconSpinnerAdapter = new IconSpinnerAdapter(spinnerView);
        spinnerView.setSpinnerAdapter(iconSpinnerAdapter);
        spinnerView.setItems(iconSpinnerItems);
        spinnerView.selectItemByIndex(0);
//        spinnerView.setLifecycleOwner(this);

        spinnerView.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(int i, Object o) {
                IconSpinnerItem it = iconSpinnerItems.get(i);
                System.out.println(it.component2());


            }


        });


    }


}
