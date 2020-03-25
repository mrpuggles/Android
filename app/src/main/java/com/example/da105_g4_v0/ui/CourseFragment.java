package com.example.da105_g4_v0.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.course.Course;
import com.example.da105_g4_v0.course.CourseDetailsActivity;
import com.example.da105_g4_v0.course.CourseMapActivity;
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
import java.util.List;


public class CourseFragment extends Fragment {
    private static String TAG="courseFragment";
    private Toolbar toolbar;
    private CommonTask getCourseTask, getCoursePhoto;
    private RecyclerView recyclerView;
    private OneImageTask getCourseImageTask;
    private Gson gson;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_course, container, false);
        setHasOptionsMenu(true);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        recyclerView = rootView.findViewById(R.id.rv_course);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        toolbar =  rootView.findViewById(R.id.tb_course);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("");

        return rootView;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.course_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id==R.id.map_item){

            Intent intent = new Intent(getActivity(), CourseMapActivity.class);
            startActivity(intent);

            return true;
        }
        return false;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(Util.networkConnected(getActivity())){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");

            String jsonOut = jsonObject.toString();


            updateUI(jsonOut);

        } else{
            Toast.makeText(getActivity(), "no connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String jsonOut){
        getCourseTask = new CommonTask(Util.URL+"/course/course.do", jsonOut);

        List<Course> courseList = null;


        try{
            String jsonIn = getCourseTask.execute().get();
            Type listType = new TypeToken<List<Course>>(){}.getType();
            courseList =  gson.fromJson(jsonIn, listType);

        } catch(Exception e){
            Log.e(TAG, e.toString());}

            if(courseList == null || courseList.isEmpty()){
//                Toast.makeText(getActivity(), "course not found", Toast.LENGTH_SHORT).show();
                return;
            }

            else{
                recyclerView.setAdapter(new CourseAdapter(getActivity(), courseList));
            }


    }


    private class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
        private LayoutInflater inflater;
        private Context context;
        private List<Course> courseList;
        private int imageSize;

        public CourseAdapter(Context context, List<Course> courseList){
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.courseList = courseList;
            imageSize = getResources().getDisplayMetrics().widthPixels/6;

        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_courseName, tv_courseStrtTime, tv_CourseEndTime, tv_CoursePrice;
            ImageView iv_coursePic;

            public ViewHolder(View view){
                super(view);
                tv_courseName = view.findViewById(R.id.tv_courseName);
                tv_courseStrtTime = view.findViewById(R.id.tv_courseStartTime);
                tv_CourseEndTime = view.findViewById(R.id.tv_courseEndTime);
                tv_CoursePrice = view.findViewById(R.id.tv_coursePrice);

                iv_coursePic = view.findViewById(R.id.iv_course);

            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_main_course, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final Course course = courseList.get(position);

            String url = Util.URL +"/course_photo/course_photo.do";
            String course_no = course.getCour_no();

            getCourseImageTask = new OneImageTask(url, course_no, imageSize, viewHolder.iv_coursePic);
            getCourseImageTask.execute();

            viewHolder.tv_courseName.setText(course.getCour_name());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd:HH:mm");

            String startDate = format.format(course.getCour_time());
            String endDate =format.format(course.getCour_endtime());

            viewHolder.tv_courseStrtTime.setText(startDate);
            viewHolder.tv_CourseEndTime.setText(endDate);
            viewHolder.tv_CoursePrice.setText("$ ".concat(String.valueOf(course.getCour_price())));


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CourseDetailsActivity.class);
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


}
