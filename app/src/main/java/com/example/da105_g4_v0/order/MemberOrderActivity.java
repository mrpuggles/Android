package com.example.da105_g4_v0.order;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.google.android.material.tabs.TabLayout;



public class MemberOrderActivity extends AppCompatActivity {
    private static final String TAG = "MemberOrderActivity";
    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int[] tabIcons = {
            R.drawable.ic_outlined_ongoing, R.drawable.ic_outlined_ok, R.drawable.ic_outlined_canceled
    };
    private Fragment[] mFragmentList = {
            new MemberOrderOngoing(), new MemberOrderComplete(), new MemberOrderCanceled()
    };
    private String[] title = {"處理中", "已完成", "已取消"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_order);
        setTabPager();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }


    //FragmentPagerAdapter.POSITION_NONE
    private void setTabPager() {
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentList[position];
            }

            @Override
            public int getCount() {
                return mFragmentList.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }


        });
        mTabLayout.setupWithViewPager(mViewPager, true);


        for (int position = 0; position < title.length; position++) {
            mTabLayout.getTabAt(position).setText(title[position]).setIcon(tabIcons[position]);
        }
    }


}