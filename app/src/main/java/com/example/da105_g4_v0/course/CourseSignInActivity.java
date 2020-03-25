package com.example.da105_g4_v0.course;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.main.Contents;
import com.example.da105_g4_v0.main.QRCodeEncoder;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import java.lang.reflect.Type;

public class CourseSignInActivity extends AppCompatActivity {
    private CommonTask getCourseDetail;
    private ImageView ivCode;
    private OneImageTask courseImageTask;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_sign_in);
        gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        Course course = (Course) getIntent().getSerializableExtra("course");
        ivCode = findViewById(R.id.iv_code);
        getMemberCourseDetail(course);
        showDetails(course);
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
                finish();
                return true;

            case R.id.home_item:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return false;
    }


    private void getMemberCourseDetail(Course course){
        SharedPreferences pref = this.getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        System.out.println(memNo);
        String courseNo= course.getCour_no();
        System.out.println(courseNo);
        if(Util.networkConnected(this)){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getMemberCourseDetail");
            jsonObject.addProperty("memNo", memNo);
            jsonObject.addProperty("courseNo", courseNo);

            String jsonOut = jsonObject.toString();

            String url = Util.URL+"/course_detail/course_detail.do";
            getCourseDetail = new CommonTask(url, jsonOut);
            CourseDetail courseDetail = null;
            try{

                String jsonIn = getCourseDetail.execute().get();
                Type typeVO = new TypeToken<CourseDetail>(){}.getType();
                 courseDetail = new CourseDetail();
                 courseDetail =  gson.fromJson(jsonIn, typeVO);
            }catch(Exception e){e.toString();}


            String cour =courseDetail.getCour_no();
            JsonObject jsonObject1 = new JsonObject();

            jsonObject1.addProperty("mem", memNo);
            jsonObject1.addProperty("cour", cour);

            String jsonOut1 = jsonObject1.toString();


            getDetailForQR(jsonOut1);
        }

    }

    private void showDetails(final Course course) {
        ImageView iv = findViewById(R.id.iv_course_signIn);
        String url = Util.URL + "/course_photo/course_photo.do";
        String cour_no = course.getCour_no();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        Bitmap bitmap = null;

        try {
            courseImageTask = new OneImageTask(url, cour_no, imageSize);
            bitmap = courseImageTask.execute().get();

        } catch (Exception e) {
           e.toString();
        }

        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            iv.setImageResource(R.drawable.default_image);
        }

        TextView tv = findViewById(R.id.tv_course_signIn);
        String courseInfo = "課程名稱: " + course.getCour_name()
                + "\n" + "\n" + "課程內容: " + course.getCour_info()
                + "\n" + "\n" + "最少開課人數: " + course.getCour_minjoin()
                + "\n" + "\n" + "目前已報名人數: " + course.getCour_count()
                + "/" + course.getCour_maxjoin();

        tv.setText(courseInfo);


    }

    private int getDimension() {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // 取得螢幕尺寸
        Display display = manager.getDefaultDisplay();
        // API 13列為deprecated，但為了支援舊版手機仍採用
        int width = display.getWidth();
        int height = display.getHeight();

        // 產生的QR code圖形尺寸(正方形)為螢幕較短一邊的1/2長度
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension / 2;

        // API 13開始支援
//                Display display = manager.getDefaultDisplay();
//                Point point = new Point();
//                display.getSize(point);
//                int width = point.x;
//                int height = point.y;
//                int smallerDimension = width < height ? width : height;
//                smallerDimension = smallerDimension / 2;
        return smallerDimension;
    }

    private void getDetailForQR(String jsonIn){
        int smallerDimension = getDimension();
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(jsonIn, null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ivCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (getCourseDetail != null) {
            getCourseDetail.cancel(true);
        }

        if (courseImageTask != null) {
            courseImageTask.cancel(true);
        }




    }

}
