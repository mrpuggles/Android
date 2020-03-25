package com.example.da105_g4_v0.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import com.example.da105_g4_v0.R;

import com.example.da105_g4_v0.bid.AuctionDetailsActivity;
import com.example.da105_g4_v0.bid.MemberBidDetailsActivity;
import com.example.da105_g4_v0.course.MemberCourseDetailsActivity;
import com.example.da105_g4_v0.friendlist.MemberFriendList;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.member.LoginActivity;
import com.example.da105_g4_v0.member.MemberInfoActivity;
import com.example.da105_g4_v0.order.MemberOrderActivity;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


public class MemberFragment extends Fragment {

    private TextView textView;
    private CircleImageView circleImageView;
    private Button order, course, bid, friend, basic, logOut;
    private Toolbar toolbar;
    private ImageTask getProfileTask;
    private OneImageTask oneImageTask;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       View rootView = inflater.inflate(R.layout.fragment_member, container, false);
        setHasOptionsMenu(true);
//        textView = rootView.findViewById(R.id.member_tv);
//        String title = getArguments().getString("title", "");
//        textView.setText(title);

        circleImageView = rootView.findViewById(R.id.profile_image);
        order=rootView.findViewById(R.id.order_query_btn);
        course =rootView.findViewById(R.id.course_management_btn);
        bid = rootView.findViewById(R.id.bid_management_btn);
        friend = rootView.findViewById(R.id.friend_management_btn);
        basic =  rootView.findViewById(R.id.basic_info_btn);
        logOut = rootView.findViewById(R.id.logOut_btn);
        order.setOnClickListener(memberPageOnClickListener);
        course.setOnClickListener(memberPageOnClickListener);
        bid.setOnClickListener(memberPageOnClickListener);
        friend.setOnClickListener(memberPageOnClickListener);
        basic.setOnClickListener(memberPageOnClickListener);
        logOut.setOnClickListener(memberPageOnClickListener);
        toolbar =  rootView.findViewById(R.id.tb_member);
        textView = rootView.findViewById(R.id.member_name);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolBarActions();
        toolbar.setTitle("");
        return rootView;
    }
    @Override
    public void onStart(){
        super.onStart();
        String url2 = Util.URL + "/member/member.do";
        int imageSizex = getResources().getDisplayMetrics().widthPixels / 4;
        Bitmap profile = null;
        SharedPreferences pref = MemberFragment.this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String mem_no = pref.getString("member_no", "");
        String mem_ac = pref.getString("member_ac", "");
        try {

            oneImageTask = new OneImageTask(url2, mem_no, imageSizex);
            profile = oneImageTask.execute().get();


        } catch (Exception e) {

        }

        if (profile != null) {
            circleImageView.setImageBitmap(profile);
        } else {
            circleImageView.setImageResource(R.drawable.ic_wine_v1n);
        }

        textView.setText(mem_ac);
    }

    private void toolBarActions(){

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.item_member_chat:
                        Intent intent = new Intent();
                        startActivity(intent);

                        break;

                }

                return true;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.member_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


        View.OnClickListener memberPageOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.order_query_btn:
                        Intent intent1 = new Intent(getActivity(), MemberOrderActivity.class);
//                    Bundle bundle1 = new Bundle();
//                    intent1.putExtras(bundle1);
                        startActivity(intent1);

                        break;

                    case R.id.course_management_btn:
                        Intent intent2 = new Intent(getActivity(), MemberCourseDetailsActivity.class);
//                    Bundle bundle1 = new Bundle();
//                    intent1.putExtras(bundle1);
                        startActivity(intent2);
                        break;
                    case R.id.bid_management_btn:
                        Intent intent3 = new Intent(getActivity(), MemberBidDetailsActivity.class);
//                    Bundle bundle1 = new Bundle();
//                    intent1.putExtras(bundle1);
                        startActivity(intent3);
                        break;

                    case R.id.friend_management_btn:
                        Intent intent4 = new Intent(getActivity(), MemberFriendList.class);
//                    Bundle bundle1 = new Bundle();
//                    intent1.putExtras(bundle1);
                        startActivity(intent4);
                        break;

                    case R.id.basic_info_btn:
                        Intent intent5 = new Intent(getActivity(), MemberInfoActivity.class);
//                    Bundle bundle1 = new Bundle();
//                    intent1.putExtras(bundle1);
                        startActivity(intent5);

                    case R.id.logOut_btn:
                        SharedPreferences pref = MemberFragment.this.getActivity().getSharedPreferences(Util.PREF_FILE,
                                MODE_PRIVATE);
                        pref.edit().putBoolean("login", false).apply();
                        view.setVisibility(View.INVISIBLE);
                        Intent intent6 = new Intent(MemberFragment.this.getActivity(), LoginActivity.class);
                        startActivity(intent6);
                        break;
                }
            }
        };



}

