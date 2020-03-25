package com.example.da105_g4_v0.bid;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.da105_g4_v0.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MemberUnpaidItems extends Fragment {


    public MemberUnpaidItems() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_unpaid_items, container, false);
    }

}
