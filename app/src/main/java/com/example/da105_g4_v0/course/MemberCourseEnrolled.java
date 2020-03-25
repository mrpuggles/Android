package com.example.da105_g4_v0.course;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.task.CommonTask;
import com.example.da105_g4_v0.task.ImageTask;
import com.example.da105_g4_v0.task.OneImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class MemberCourseEnrolled extends Fragment {


    private static final String TAG = "MemberCourseEnrolled";
    private RecyclerView recyclerView;
    private CommonTask getCourseEnrolledList, getCourseList;
    private OneImageTask getCourseImageTask;
    private String memNumber;
    private CourseEnrolledAdapter adapter;
    private Gson gson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");
        memNumber = memNo;
        View rootView = inflater.inflate(R.layout.fragment_member_course_enrolled, container, false);

        recyclerView = rootView.findViewById(R.id.rv_courseEnrolled);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences pref = this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
        String memNo = pref.getString("member_no", "");


        updateUI(memNo);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            SharedPreferences pref = this.getActivity().getSharedPreferences(Util.PREF_FILE, MODE_PRIVATE);
//            String memNo = pref.getString("member_no", "");
//            updateUI(memNo);
//
//        }
//    }


    private void updateUI(String memNo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getCourseEnrolled");
        jsonObject.addProperty("memNo", memNo);

        String jsonOut = jsonObject.toString();

        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("action", "getAll");


        String url = Util.URL + "/course_detail/course_detail.do";

        getCourseEnrolledList = new CommonTask(url, jsonOut);

        List<Course> courseSelectedList = null;

        try {
            String jsonIn = getCourseEnrolledList.execute().get();

            Type listType = new TypeToken<List<Course>>() {
            }.getType();
            courseSelectedList = gson.fromJson(jsonIn, listType);
            Log.e(TAG, "updateUI: jsonIn " + jsonIn);


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }


        if (courseSelectedList == null || courseSelectedList.isEmpty()) {

            try {
                adapter.clearData();
            } catch (Exception e) {
            }


        } else {
            if (adapter == null) {
                adapter = new CourseEnrolledAdapter(MemberCourseEnrolled.this.getActivity(), courseSelectedList);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.setData(courseSelectedList);
                recyclerView.setAdapter(adapter);
            }

        }
    }


    private class CourseEnrolledAdapter extends RecyclerView.Adapter<MemberCourseEnrolled.CourseEnrolledAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<Course> courseSelectedList;

        private int imageSize;


        public CourseEnrolledAdapter(Context context, List<Course> courseSelectedList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.courseSelectedList = courseSelectedList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_courseName, tv_courseStartTime, tv_courseEndTime, tv_tuition;
            ImageView iv_coursePic;


            public ViewHolder(View view) {
                super(view);
                tv_courseName = view.findViewById(R.id.tv_courseEnrolledM);
                tv_courseStartTime = view.findViewById(R.id.tv_courseStartTimeM);
                tv_courseEndTime = view.findViewById(R.id.tv_courseEndTimeM);
                tv_tuition = view.findViewById(R.id.tv_coursePriceM);
                iv_coursePic = view.findViewById(R.id.iv_courseEnrolled);


            }
        }


        @Override
        public MemberCourseEnrolled.CourseEnrolledAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_course_enrolled, parent, false);
            MemberCourseEnrolled.CourseEnrolledAdapter.ViewHolder viewHolder = new MemberCourseEnrolled.CourseEnrolledAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(MemberCourseEnrolled.CourseEnrolledAdapter.ViewHolder viewholder, int position) {
            final Course course = courseSelectedList.get(position);

            String url = Util.URL + "/course_photo/course_photo.do";
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
            String courseName = course.getCour_name();
            String startTime = format.format(course.getCour_starttime());
            String endTime = format.format(course.getCour_endtime());
            String courseNo = course.getCour_no();
            viewholder.tv_courseName.setText("課程名稱: " + courseName);
            viewholder.tv_courseStartTime.setText("開始時間: " + startTime);
            viewholder.tv_courseEndTime.setText("結束時間: " + endTime);
            viewholder.tv_tuition.setText("報名費: $ ".concat(String.valueOf(course.getCour_price())));

            getCourseImageTask = new OneImageTask(url, courseNo, imageSize, viewholder.iv_coursePic);
            getCourseImageTask.execute();


            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),
                            CourseSignInActivity.class);
                    intent.putExtra("course", course);
                    startActivity(intent);

                }
            });


        }

        @Override
        public int getItemCount() {
            return courseSelectedList == null ? 0 : courseSelectedList.size();
        }

        public void setData(List<Course> courseSelectedList) {
            this.courseSelectedList = courseSelectedList;
            notifyDataSetChanged();
        }

        public void clearData() {
            if (this.courseSelectedList.size() > 0)
                this.courseSelectedList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getCourseEnrolledList != null) {
            getCourseEnrolledList.cancel(true);
        }

        if (getCourseList != null) {
            getCourseList.cancel(true);
        }

        if (getCourseImageTask != null) {
            getCourseImageTask.cancel(true);
        }


    }
}
