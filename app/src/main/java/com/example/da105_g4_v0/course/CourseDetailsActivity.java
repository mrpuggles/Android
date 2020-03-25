package com.example.da105_g4_v0.course;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da105_g4_v0.MainActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.cart.ShoppingCartActivity;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static String TAG = "CourseDetailActivity";
    private OneImageTask courseImageTask;
    private Button btn_enroll;
    private Gson gson;
    private CommonTask newEnroll, checkCourse, checkTotalCount, updateEnroll, getCourseEnrolled, getCourses;
    private GoogleMap map;
    private String courseAddress;
    private Course course;
    private net.cachapa.expandablelayout.ExpandableLayout el_detail;
    private LinearLayout linear;
    private ScrollView sv_course;
    private FusedLocationProviderClient fusedLocationProviderClient;
//    private Location myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fmMap);

        mapFragment.getMapAsync(this);

        Course course = (Course) getIntent().getSerializableExtra("course");
        sv_course= findViewById(R.id.sv_course_detail);
        sv_course.setVerticalScrollBarEnabled(true);
        sv_course.setNestedScrollingEnabled(true);
        el_detail = findViewById(R.id.expandable_layout_course_detail);
        linear = findViewById(R.id.linear_course_detail);
        btn_enroll = findViewById(R.id.btn_enroll);


        if (course == null) {
//            Toast.makeText(this, "course not found", Toast.LENGTH_SHORT).show();
        } else {
            showDetails(course);
            this.course = course;

        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        courseAddress = course.getCour_address().trim();


    }

    public void onDetailClick(View view){
        el_detail.toggle();
    }

    @Override
    public void onStart() {
        super.onStart();

        onEnroll(this.course);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
    }

    @SuppressLint("MissingPermission")
    private void setUpMap() {
        map.setTrafficEnabled(true);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        locationNameToMarker(courseAddress);
    }


    private void locationNameToMarker(String locationName) {
        // 增加新標記前，先清除舊有標記
        map.clear();
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            // 解譯地名/地址後可能產生多筆位置資訊，所以回傳List<Address>
            // 將maxResults設為1，限定只回傳1筆
            addressList = geocoder.getFromLocationName(locationName, maxResults);
//            geocoder.getFromLocation();

            // 如果無法連結到提供服務的伺服器，會拋出IOException
        } catch (IOException ie) {
            Log.e(TAG, ie.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
//            Toast.makeText(this, "not found", Toast.LENGTH_SHORT).show();
        } else {
            // 因為當初限定只回傳1筆，所以只要取得第1個Address物件即可
            Address address = addressList.get(0);

            // Address物件可以取出緯經度並轉成LatLng物件
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            // 將地址取出當作標記的描述文字
            String snippet = address.getAddressLine(0);

            String code = address.getPostalCode();


            // 將地名或地址轉成位置後在地圖打上對應標記
            map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(code + locationName)
                    .snippet(snippet));

            // 將鏡頭焦點設定在使用者輸入的地點上
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(15)
                    .build();

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
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

    public void onEnroll(final Course course) {

        btn_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseDetailsActivity.this);

                // Set a title for alert dialog
                builder.setTitle("Confirm Enrollment");

                // Ask the final question
                builder.setMessage("Please Confirm to enroll");

                builder.setIcon(R.drawable.ic_wine_v1);

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                SharedPreferences pref = getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
                                String memNo = pref.getString("member_no", "");
                                Integer currentlyEnrolled = 0;
                                if (Util.networkConnected(CourseDetailsActivity.this)) {
                                    String url2 = Util.URL + "/course/course.do";
                                    JsonObject jsonObject2 = new JsonObject();
                                    jsonObject2.addProperty("action", "checkCourseTotalCount");
                                    jsonObject2.addProperty("courseNo", course.getCour_no());
                                    String jsonOut2 = jsonObject2.toString();
                                    checkTotalCount = new CommonTask(url2, jsonOut2);

                                    try {

                                        currentlyEnrolled = Integer.valueOf(checkTotalCount.execute().get());

                                    } catch (Exception e) {
                                        e.toString();
                                    }
                                }


                                if ((currentlyEnrolled + 1) > course.getCour_maxjoin()) {

                                    LayoutInflater inflater = getLayoutInflater();
                                    View view1 = inflater.inflate(R.layout.dialog_pop, null, false);
                                    new AlertDialog.Builder(CourseDetailsActivity.this)
                                            .setMessage("人數已滿")
                                            .setIcon(R.drawable.ic_wine_v1n)
                                            .setView(view1)
                                            .show();


                                    return;
                                }


                                if (Util.networkConnected(CourseDetailsActivity.this)) {
                                    String url = Util.URL + "/course_detail/course_detail.do";
                                    String url2 = Util.URL + "/course/course.do";

                                    String courseNo = course.getCour_no();
                                    JsonObject jsonObject1 = new JsonObject();
                                    jsonObject1.addProperty("action", "getMemberCourse");
                                    jsonObject1.addProperty("memberNo", memNo);
                                    jsonObject1.addProperty("courseNo", courseNo);
                                    String jsonOut1 = jsonObject1.toString();
                                    checkCourse = new CommonTask(url, jsonOut1);

                                    Boolean isEnrolled = true;
                                    try {
                                        isEnrolled = Boolean.valueOf(checkCourse.execute().get());

                                    } catch (Exception e) {
                                        e.toString();
                                    }


                                    if (isEnrolled == false) {


                                       //從明細取出已報名課程
                                        JsonObject jsonObject4 = new JsonObject();
                                        jsonObject4.addProperty("action", "getCourseEnrolled");
                                        jsonObject4.addProperty("memNo", memNo);
                                        String jsonOut4 = jsonObject4.toString();
                                        getCourseEnrolled = new CommonTask(url, jsonOut4);
                                        List<CourseDetail> detailList = null;
                                        try {
                                            String jsonIn4 = getCourseEnrolled.execute().get();
                                            Type listType = new TypeToken<List<CourseDetail>>() {
                                            }.getType();
                                            detailList = gson.fromJson(jsonIn4, listType);

                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }

                                        if(detailList ==null || detailList.isEmpty()){

                                            Enroll(url2, url,memNo, currentlyEnrolled);

                                            return;
                                        }


                                        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");


                                        //目前欲報名之課程
                                        long selectCourtStartTime = 0;
                                        String selectCourse = formatter.format(course.getCour_time());
                                        System.err.println("selectCourse                            "+selectCourse);
                                        Date selectedCourseStartTime =null;
                                        try {
                                            selectedCourseStartTime = formatter.parse(selectCourse);
                                            selectCourtStartTime = selectedCourseStartTime.getTime();
                                        }catch(Exception e){}
                                        double selectCourHourDouble = course.getCour_hours()*3600000;

                                        long selectCourHourInt = (long)selectCourHourDouble;
                                        long selectCourEnd = selectCourtStartTime +selectCourHourInt ;
                                        String selectCourseEndDateString = formatter.format(selectCourEnd);

                                        Date selectCourseEndDate = null;
                                        try {
                                            selectCourseEndDate = formatter.parse(selectCourseEndDateString);
                                        }catch(Exception e){}


                                        //取出課程編號並裝進list,去資料庫把該課程時間拿回來
                                        List<String> courNoList = new ArrayList<>();
                                        for (CourseDetail cd : detailList) {
                                            courNoList.add(cd.getCour_no().trim());
                                        }
                                        String courList = new Gson().toJson(courNoList);
                                        JsonObject jsonObject5 = new JsonObject();
                                        jsonObject5.addProperty("action", "getCourses");
                                        jsonObject5.addProperty("courNoList", courList);
                                        String jsonOut5 = jsonObject5.toString();
                                        getCourses = new CommonTask(url2, jsonOut5);
                                        List<Course> coursesList = null;
                                        String jsonIn5= null;
                                        try {

                                            jsonIn5 = getCourses.execute().get();


                                        } catch (Exception e) {
                                        }

                                        Type listType1 = new TypeToken<List<Course>>(){}.getType();
                                        coursesList =gson.fromJson(jsonIn5, listType1);



                                        long courTimeLong =0;
                                        for(Course courses : coursesList){
                                            String courTime = formatter.format(courses.getCour_time());
                                            Date courseStartTime=null;
                                            try {
                                                courseStartTime = formatter.parse(courTime);
                                                courTimeLong = courseStartTime.getTime();

                                            } catch (Exception e) {
                                            }
                                            double courHourDouble = courses.getCour_hours()*3600000;
                                            int courHourInt = (int)courHourDouble;
                                            long courEndTime = courTimeLong+courHourInt;
                                            String CourseEndDateString = formatter.format(courEndTime);

                                            Date CourseEndDate = null;
                                            try {
                                                CourseEndDate = formatter.parse(CourseEndDateString);
                                            }catch(Exception e){}
//
                                            if (selectCourseEndDate.getTime() >=courseStartTime.getTime()
                                                    && courseStartTime.getTime() <= selectCourseEndDate.getTime()
                                                    || selectedCourseStartTime.getTime() <= CourseEndDate.getTime() && selectedCourseStartTime.getTime() >=courseStartTime.getTime()){
                                                LayoutInflater inflater = getLayoutInflater();
                                                View view1 = inflater.inflate(R.layout.dialog_pop, null, false);

                                                new AlertDialog.Builder(CourseDetailsActivity.this)
                                                        .setMessage("上課時間有衝突, 請選擇其他課程")
                                                        .setIcon(R.drawable.ic_wine_v1n)
                                                        .setView(view1)
                                                        .show();


                                                return;

                                            } else{
//
                                                Enroll(url2, url,memNo, currentlyEnrolled);

                                            }

                                        }


                                    } else {

                                        LayoutInflater inflater = getLayoutInflater();
                                        View view1 = inflater.inflate(R.layout.dialog_pop, null, false);
                                        new AlertDialog.Builder(CourseDetailsActivity.this)
                                                .setMessage("已報名")
                                                .setIcon(R.drawable.ic_wine_v1n)
                                                .setView(view1)
                                                .show();


                                    }
                                }


                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Confirm", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Cancel", dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }


        });

    }

    private void Enroll(String url2, String url, String memNo, int currentlyEnrolled){

        JsonObject jsonObject3 = new JsonObject();
        jsonObject3.addProperty("action", "updateCourse");
        jsonObject3.addProperty("courseNo", course.getCour_no());
        jsonObject3.addProperty("addEnroll", String.valueOf((currentlyEnrolled + 1)));
        String jsonOut3 = jsonObject3.toString();
        updateEnroll = new CommonTask(url2, jsonOut3);
        updateEnroll.execute();

        CourseDetail courseDetail = new CourseDetail();
        courseDetail.setMem_no(memNo);
        courseDetail.setCour_no(course.getCour_no());
        String enroll = gson.toJson(courseDetail);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "newEnroll");
        jsonObject.addProperty("enroll", enroll);
        String jsonOut = jsonObject.toString();
        newEnroll = new CommonTask(url, jsonOut);
        CourseDetail successfulEnroll = null;
        newEnroll.execute();

        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialog_pop, null, false);
        new AlertDialog.Builder(CourseDetailsActivity.this)
                .setMessage("報名成功")
                .setIcon(R.drawable.ic_wine_v1n)
                .setView(view1)
                .show();


    }
    private void showDetails(final Course course) {
        ImageView iv = findViewById(R.id.iv_course_detail);
        String url = Util.URL + "/course_photo/course_photo.do";
        String cour_no = course.getCour_no();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 6;
        Bitmap bitmap = null;

        try {
            courseImageTask = new OneImageTask(url, cour_no, imageSize);
            bitmap = courseImageTask.execute().get();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            iv.setImageResource(R.drawable.default_image);
        }
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String courTimestr = formatter.format(course.getCour_time());
        TextView tv = findViewById(R.id.tv_course_detail);
        String hours = String.valueOf(course.getCour_hours());

        String courseInfo = "課程名稱: " + course.getCour_name()
                + "\n" + "\n" + "課程內容: " + course.getCour_info()
                + "\n"+ "\n" +"開課日期: "+courTimestr
                + "\n"+ "\n" +"課程時數: "+  hours
                + "\n" + "\n" + "最少開課人數: " + course.getCour_minjoin()
                + "\n" + "\n" + "目前已報名人數: " + course.getCour_count()
                + "/" + course.getCour_maxjoin();

        tv.setText(courseInfo);


    }

    private Address getAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        try {
            addressList = geocoder.getFromLocationName(address, 1);
        } catch (Exception e) {
            e.toString();
        }


        if (addressList == null || addressList.isEmpty())
            return null;
        else

            return addressList.get(0);
    }


    public void onNaviClicked(View view) {

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {

                    @Override
                    public void onSuccess(Location location) {
                        Address address = getAddress(courseAddress);

                        double fromLat = location.getLatitude();
                        double fromLng = location.getLongitude();
                        double toLat = address.getLatitude();
                        double toLng = address.getLongitude();
                        direct(fromLat, fromLng, toLat, toLng);
                    }
                });


    }

    // 開啟Google地圖應用程式來完成導航要求
    private void direct(double fromLat, double fromLng,
                        double toLat, double toLng) {
        // 設定欲前往的Uri，saddr-出發地緯經度；daddr-目的地緯經度
        String uriStr = String.format(Locale.TAIWAN,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f",
                fromLat, fromLng, toLat, toLng);
        Intent intent = new Intent();
        // 指定交由Google地圖應用程式接手
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        // ACTION_VIEW-呈現資料給使用者觀看
        intent.setAction(Intent.ACTION_VIEW);
        // 將Uri資訊附加到Intent物件上
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();


        if (courseImageTask != null) {
            courseImageTask.cancel(true);
        }

        if (newEnroll != null) {
            newEnroll.cancel(true);
        }

        if (checkCourse != null) {
            checkCourse.cancel(true);
        }

        if (checkTotalCount != null) {
            checkTotalCount.cancel(true);
        }

        if (updateEnroll != null) {
            updateEnroll.cancel(true);
        }

        if ( getCourseEnrolled != null) {
            getCourseEnrolled.cancel(true);
        }

        if ( getCourses != null) {
            getCourses.cancel(true);
        }

    }


}
