package com.example.da105_g4_v0.member;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.MainVendorActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.vendor.Vendor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;



public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private CommonTask isMemberTask, getMemberInfoTask, isVendorTask, getVendorInfoTask;
    private  Member member = null;
    private Vendor vendor = null;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setResult(RESULT_CANCELED);
        gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    }

    public void onLoginClicked(View view){

        EditText editEmail = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editPassWord);
        String memberEmail = editEmail.getText().toString().trim();
        String memberPassword = editPassword.getText().toString().trim();
        if(memberEmail.length() <= 0 || memberPassword.length() <=0){
            Toast.makeText(this, "請輸入帳號或密碼", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isMember(memberEmail, memberPassword)){

            if(Util.networkConnected(this)){

                String url = Util.URL+"/member/member.do";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getMemberInfo");
                jsonObject.addProperty("memberEmail", memberEmail);
                String jsonOut = jsonObject.toString();
                getMemberInfoTask = new CommonTask(url, jsonOut);

                try {

                    String jsonIn = getMemberInfoTask.execute().get();
                    Type typeVO = new TypeToken<Member>(){}.getType();
                    member =  gson.fromJson(jsonIn, typeVO);

                }catch(Exception e){Log.e(TAG, e.toString());}

            }
            String memberNo = member.getMem_no();
            String memName = member.getMem_name();
            String member_ac = member.getMem_id();
            SharedPreferences preferences = getSharedPreferences(
                    Util.PREF_FILE, MODE_PRIVATE);

            preferences.edit().putBoolean("login", true)
                    .putString("memberEmail", memberEmail)
                    .putString("memberPassword", memberPassword)
                    .putString("member_name", memName)
                    .putString("member_no", memberNo)
                    .putString("member_ac", member_ac)
                    .apply();
            setResult(RESULT_OK);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();}

    }

    private boolean isMember(String memberEmail, String memberPassword){

        boolean isMember = false;
        if(Util.networkConnected(this)){
            String url = Util.URL+"/member/member.do";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isMember");
            jsonObject.addProperty("memberEmail", memberEmail);
            jsonObject.addProperty("memberPassword", memberPassword);
            String jsonOut = jsonObject.toString();
            isMemberTask = new CommonTask(url, jsonOut);

            try{
                String result = isMemberTask.execute().get();
                isMember = Boolean.valueOf(result);

            }catch(Exception e){
                Log.e(TAG, e.toString());
            isMember = false;
            }



        } else{Util.showToast(this, R.string.no_network);}

        return isMember;
    }

    public void onVendorClicked(View view){

        EditText editID = findViewById(R.id.editEmail);
        EditText editPassword = findViewById(R.id.editPassWord);
        String vendorID = editID.getText().toString().trim();
        String vendorPassword = editPassword.getText().toString().trim();
        if(vendorID.length() <= 0 || vendorPassword.length() <=0){
            Toast.makeText(this, "請輸入帳號或密碼", Toast.LENGTH_SHORT).show();
            return;
        }
        if(isVendor(vendorID, vendorPassword)){

            if(Util.networkConnected(this)){

                String url = Util.URL+"/vendor/vendor.do";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getVendorInfo");
                jsonObject.addProperty("vendorID", vendorID);
                String jsonOut = jsonObject.toString();
                getVendorInfoTask = new CommonTask(url, jsonOut);

                try {

                    String jsonIn =  getVendorInfoTask.execute().get();
                    Type typeVO = new TypeToken<Vendor>(){}.getType();
                    vendor =  gson.fromJson(jsonIn, typeVO);

                }catch(Exception e){Log.e(TAG, e.toString());}

            }
            String vendorNo = vendor.getVen_no();

            SharedPreferences preferences = getSharedPreferences(
                    Util.PREF_FILE, MODE_PRIVATE);

            preferences.edit().putBoolean("login", true)
                    .putString("vendorID", vendorID)
                    .putString("vendorPassword", vendorPassword)
                    .putString("vendor_no", vendorNo).apply();
            setResult(RESULT_OK);
            Intent intent = new Intent(LoginActivity.this, MainVendorActivity.class);
            startActivity(intent);
            finish();
        }else{Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();}

    }

    private boolean isVendor(String vendorID, String vendorPassword){

        boolean isVendor = false;
        if(Util.networkConnected(this)){
            String url = Util.URL+"/vendor/vendor.do";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "isVendor");
            jsonObject.addProperty("vendorID", vendorID);
            jsonObject.addProperty("vendorPassword", vendorPassword);
            String jsonOut = jsonObject.toString();
            isVendorTask = new CommonTask(url, jsonOut);

            try{
                String result = isVendorTask.execute().get();
                isVendor = Boolean.valueOf(result);

            }catch(Exception e){
                Log.e(TAG, e.toString());
                isVendor = false;
            }



        } else{Util.showToast(this, R.string.no_network);}

        return isVendor;
    }

    protected void onStop(){
        super.onStop();
        if(isMemberTask !=null){
            isMemberTask.cancel(true);
        }

        if(isVendorTask !=null){
            isVendorTask.cancel(true);
        }
        if(getMemberInfoTask !=null){
            getMemberInfoTask.cancel(true);
        }
        if(getVendorInfoTask !=null){
            getVendorInfoTask.cancel(true);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        Log.e(TAG, "login=" + login);
        if (login) {
            String account = preferences.getString("memberEmail", "");
            String password = preferences.getString("memberPassword", "");
            Log.e(TAG, account + "  " + password);
            if (isMember(account, password)) {
                setResult(RESULT_OK);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }
    }
}


