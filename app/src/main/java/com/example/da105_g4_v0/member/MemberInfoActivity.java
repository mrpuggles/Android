package com.example.da105_g4_v0.member;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.da105_g4_v0.R;



public class MemberInfoActivity extends AppCompatActivity {
    private TextView tv_mem_name;
    private Button btn_submit;
    private Member member_name = new Member();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        tv_mem_name = findViewById(R.id.tv_member_name);
        btn_submit = findViewById(R.id.btn_member_submit);


        member_name.setMem_name("Nick");
        tv_mem_name.setText(member_name.getMem_name());


    }


    public void onMemberNameClicked(View view){
        tv_mem_name.setCursorVisible(true);
        tv_mem_name.setFocusableInTouchMode(true);
        tv_mem_name.setInputType(InputType.TYPE_CLASS_TEXT);
        tv_mem_name.requestFocus();
        Toast.makeText(this, tv_mem_name.getText().toString(), Toast.LENGTH_SHORT).show();
        overridePendingTransition(0, 0);
    }



}
