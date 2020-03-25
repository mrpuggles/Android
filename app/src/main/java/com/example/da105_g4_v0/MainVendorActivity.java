package com.example.da105_g4_v0;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.da105_g4_v0.course.Course;
import com.example.da105_g4_v0.course.CourseDetail;
import com.example.da105_g4_v0.course.CourseDetailsActivity;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.member.LoginActivity;
import com.example.da105_g4_v0.store.OrderMaster;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.example.da105_g4_v0.ui.CourseFragment;
import com.example.da105_g4_v0.vendor.VendorSignedInMember;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainVendorActivity extends AppCompatActivity {
    private final String TAG = "MainVendorActivity";
    private Button btnScan;
    private static final String PACKAGE = "com.google.zxing.client.android";
    private Gson gson;
    private CommonTask getMemberCourseDetail, updateMemberStatus;
    private CommonTask getCourseTask;
    private RecyclerView recyclerView;
    private OneImageTask getCourseImageTask, getVendorProfile;
    private CircleImageView circleImageView;
    private TextView vendorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_vendor);

        recyclerView = findViewById(R.id.rv_vendor_sign_in);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vendorID =findViewById(R.id.tv_vendor_name);
        circleImageView = findViewById(R.id.profile_image_vendor);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        findViews();

    }

    public void onLogoutClick(View view) {
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE,
                MODE_PRIVATE);
        pref.edit().putBoolean("login", false).apply();
        view.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onStart(){
        super.onStart();

        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String venNo = pref.getString("vendor_no", "");
        String ven = pref.getString("vendorID", "");
        vendorID.setText(ven);
        if(Util.networkConnected(this)){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getVendorCourses");
            jsonObject.addProperty("venNo", venNo);

            String jsonOut = jsonObject.toString();


            updateUI(jsonOut);

        } else{
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String jsonOut){
        SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String venNo = pref.getString("vendor_no", "");
        int imageSizex = getResources().getDisplayMetrics().widthPixels / 4;
        getCourseTask = new CommonTask(Util.URL+"/course/course.do", jsonOut);
        String url2 = Util.URL+"/vendor/vendor.do";
        Bitmap profile = null;
        List<Course> courseList = null;
        try {

            getVendorProfile = new OneImageTask(url2, venNo , imageSizex);
            profile = getVendorProfile.execute().get();


        } catch (Exception e) {

        }

        if (profile != null) {
            circleImageView.setImageBitmap(profile);
        } else {
            circleImageView.setImageResource(R.drawable.ic_wine_v1n);
        }


        try{
            String jsonIn = getCourseTask.execute().get();
            Type listType = new TypeToken<List<Course>>(){}.getType();
            courseList = gson.fromJson(jsonIn, listType);

        } catch(Exception e){
            Log.e(TAG, e.toString());}

        if(courseList == null || courseList.isEmpty()){
            Toast.makeText(this, "course not found", Toast.LENGTH_SHORT).show();
        }

        else{
            recyclerView.setAdapter(new MainVendorActivity.CourseAdapter(this, courseList));
        }


    }


    private class CourseAdapter extends RecyclerView.Adapter<MainVendorActivity.CourseAdapter.ViewHolder>{
        private LayoutInflater inflater;
        private Context context;
        private List<Course> courseList;
        private int imageSize;

        public CourseAdapter(Context context, List<Course> courseList){
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.courseList = courseList;
            imageSize = getResources().getDisplayMetrics().widthPixels/4;

        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_courseName, tv_courseStrtTime, tv_CourseEndTime, tv_CoursePrice;
            ImageView iv_coursePic;

            public ViewHolder(View view){
                super(view);
                tv_courseName = view.findViewById(R.id.tv_courseName_vendor);
                tv_courseStrtTime = view.findViewById(R.id.tv_courseStartTime_vendor);
                tv_CourseEndTime = view.findViewById(R.id.tv_courseEndTime_vendor);
                tv_CoursePrice = view.findViewById(R.id.tv_coursePrice_vendor);

                iv_coursePic = view.findViewById(R.id.iv_course_vendor);

            }
        }

        @Override
        public MainVendorActivity.CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_vendor_course_sign_in, parent, false);
            MainVendorActivity.CourseAdapter.ViewHolder viewHolder = new MainVendorActivity.CourseAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MainVendorActivity.CourseAdapter.ViewHolder viewHolder, int position) {
            final Course course = courseList.get(position);

            String url = Util.URL +"/course_photo/course_photo.do";
            String course_no = course.getCour_no();

            getCourseImageTask = new OneImageTask(url, course_no, imageSize, viewHolder.iv_coursePic);
            getCourseImageTask.execute();

            viewHolder.tv_courseName.setText(course.getCour_name());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm");

            String startDate = format.format(course.getCour_starttime());
            String endDate =format.format(course.getCour_endtime());

            viewHolder.tv_courseStrtTime.setText(startDate);
            viewHolder.tv_CourseEndTime.setText(endDate);
            viewHolder.tv_CoursePrice.setText("$ ".concat(String.valueOf(course.getCour_price())));


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainVendorActivity.this, VendorSignedInMember.class);
                    intent.putExtra("course", course);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return courseList.size();
        }


    }
    @Override
    public void onStop(){
        super.onStop();

        if(getCourseTask != null){
            getCourseTask.cancel(true);
        }

        if(getCourseImageTask != null){
            getCourseImageTask.cancel(true);
        }
    }


    private void findViews() {
        btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                try {
                    startActivityForResult(intent, 0);
                }
                // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
                catch (ActivityNotFoundException ex) {
                    showDownloadDialog();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
           super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            String jsonData = "";
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");

                jsonData = contents;

                memberSignInRequest(jsonData);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(this);
        downloadDialog.setTitle("No Barcode Scanner Found");
        downloadDialog.setMessage("Please download and install Barcode Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:" + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }

    private void memberSignInRequest(String jsonData) {

        JsonObject jsonObject = new Gson().fromJson(jsonData, JsonObject.class);
        String memNo = jsonObject.get("mem").getAsString();

        String courNo = jsonObject.get("cour").getAsString();

        String url = Util.URL + "/course_detail/course_detail.do";
        if (Util.networkConnected(this)) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.addProperty("action", "getMemberCourseDetail");
            jsonObject1.addProperty("courseNo", courNo);
            jsonObject1.addProperty("memNo", memNo);

            String jsonOut = jsonObject1.toString();
            getMemberCourseDetail = new CommonTask(url, jsonOut);
            CourseDetail courseDetail = null;
            try {
                String jsonIn = getMemberCourseDetail.execute().get();
                Type typeVO = new TypeToken<CourseDetail>() {
                }.getType();
                courseDetail = new CourseDetail();
                courseDetail = gson.fromJson(jsonIn, typeVO);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            if (courseDetail.getCour_status().equals("CS0")){

                    String statusChanged = "CS1";
                    JsonObject jsonObject2 = new JsonObject();
                    jsonObject2.addProperty("action", "updateCourseDetail");
                    jsonObject2.addProperty("courseNo", courNo);
                    jsonObject2.addProperty("memNo", memNo);
                    jsonObject2.addProperty("memStatus", statusChanged);
                    String jsonOut2 = jsonObject2.toString();
                    updateMemberStatus = new CommonTask(url, jsonOut2);
                     updateMemberStatus.execute();
                    Toast.makeText(this, "報到成功", Toast.LENGTH_SHORT).show();
                }else{

                Toast.makeText(this, "已報到", Toast.LENGTH_SHORT).show();
            }

            }

        }



}
