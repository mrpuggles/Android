package com.example.da105_g4_v0;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.da105_g4_v0.ui.CourseFragment;
import com.example.da105_g4_v0.ui.HomeFragment;
import com.example.da105_g4_v0.ui.MemberFragment;
import com.example.da105_g4_v0.ui.StoreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView btmNav;
    private List<Fragment> fragments = new ArrayList<>(4);
    private static final String TAG_FRAGMENT_HOME ="tag_frag_home";
    private static final String TAG_FRAGMENT_STORE ="tag_frag_store";
    private static final String TAG_FRAGMENT_COURSE ="tag_frag_course";
    private static final String TAG_FRAGMENT_MEMBER ="tag_frag_member";
    private static final int MY_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btmNav = findViewById(R.id.nav_view);



    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();

        btmNav.setSelectedItemId(R.id.store);
        btmNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        switchFragment(0, TAG_FRAGMENT_HOME);
                        return true;

                    case R.id.store:
                        switchFragment(1, TAG_FRAGMENT_STORE);

                        return true;

                    case R.id.course:
                        switchFragment(2, TAG_FRAGMENT_COURSE);
                        return true;

                    case R.id.member:
                        switchFragment(3, TAG_FRAGMENT_MEMBER);
                        return true;
                }
                return false;
            }
        });

        buildFragmentList();
        //set the 0th fragment to be displayed by default
        switchFragment(1, TAG_FRAGMENT_STORE);

    }
    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += "";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    private HomeFragment buildHomeFragment(){
        HomeFragment fragment = new HomeFragment();
        return fragment;

    }

    private StoreFragment buildStoreFragment(){
        StoreFragment fragment = new StoreFragment();
        return fragment;

    }

    private CourseFragment buildCourseFragment(){
        CourseFragment fragment = new CourseFragment();
        return fragment;

    }

    private MemberFragment buildMemberFragment(){
        MemberFragment fragment = new MemberFragment();
        return fragment;

    }

    private void buildFragmentList(){
        HomeFragment homeFragment = buildHomeFragment();
        StoreFragment storeFragment = buildStoreFragment();
        CourseFragment courseFragment = buildCourseFragment();
        MemberFragment memberFragment = buildMemberFragment();

        fragments.add(homeFragment);
        fragments.add(storeFragment);
        fragments.add(courseFragment);
        fragments.add(memberFragment);
//
//
    }

    private void switchFragment(int pos, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout_fragmentholder, fragments.get(pos),tag)
                .commit();


    }




}


