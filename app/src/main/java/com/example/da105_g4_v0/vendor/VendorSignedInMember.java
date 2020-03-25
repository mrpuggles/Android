package com.example.da105_g4_v0.vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.da105_g4_v0.MainVendorActivity;
import com.example.da105_g4_v0.R;
import com.example.da105_g4_v0.course.Course;
import com.example.da105_g4_v0.main.Util;
import com.example.da105_g4_v0.member.Member;
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

public class VendorSignedInMember extends AppCompatActivity {

    private final String TAG = "VendorSignedInMember";
    private Gson gson;
    private CommonTask getMembers;
    private RecyclerView recyclerView;
    private OneImageTask getMembersImageTask;
    private String courseNumber;
    private TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_signed_in_member);

        Course course = (Course) getIntent().getSerializableExtra("course");
        recyclerView = findViewById(R.id.rv_vendor_memberList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseNumber = course.getCour_no();
        Log.e("courseNumber", courseNumber);

        total = findViewById(R.id.tv_vendor_signedInMembers);
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
    }


    @Override
    public void onStart() {
        super.onStart();


        if (Util.networkConnected(this)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAllMembers");
            jsonObject.addProperty("courseNo", courseNumber);

            String jsonOut = jsonObject.toString();


            updateUI(jsonOut);

        } else {
            Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(String jsonOut) {
        getMembers = new CommonTask(Util.URL+"/course_detail/course_detail.do", jsonOut);

        List<Member> memberList = null;


        try {
            String jsonIn = getMembers.execute().get();
            Type listType = new TypeToken<List<Member>>() {
            }.getType();
            memberList = gson.fromJson(jsonIn, listType);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        if (memberList == null || memberList.isEmpty()) {
//            Toast.makeText(this, "course not found", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setAdapter(new VendorSignedInMember.CourseAdapter(this, memberList));

            int totalMembers = memberList.size();
            String totalMems = String.valueOf(totalMembers);
            total.setText("報到總人數: "+totalMems);
        }


    }


    private class CourseAdapter extends RecyclerView.Adapter<VendorSignedInMember.CourseAdapter.ViewHolder> {
        private LayoutInflater inflater;
        private Context context;
        private List<Member> memberList;
        private int imageSize;

        public CourseAdapter(Context context, List<Member> memberList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.memberList = memberList;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;

        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_memberNo, tv_memberName;
            de.hdodenhof.circleimageview.CircleImageView iv_memberPic;

            public ViewHolder(View view) {
                super(view);
                tv_memberNo = view.findViewById(R.id.tv_memberNo_vendor);
                tv_memberName = view.findViewById(R.id.tv_memberName_vendor);

                iv_memberPic = view.findViewById(R.id.iv_vendor_member);

            }
        }

        @Override
        public VendorSignedInMember.CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.card_vendor_member_list, parent, false);
            VendorSignedInMember.CourseAdapter.ViewHolder viewHolder = new VendorSignedInMember.CourseAdapter.ViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(VendorSignedInMember.CourseAdapter.ViewHolder viewHolder, int position) {
            final Member member = memberList.get(position);

            String url = Util.URL+"/member/member.do";
            String mem_no = member.getMem_no();

            getMembersImageTask = new OneImageTask(url, mem_no , imageSize, viewHolder.iv_memberPic);
            getMembersImageTask.execute();

            viewHolder.tv_memberNo.setText(member.getMem_no());
            viewHolder.tv_memberName.setText(member.getMem_name());


        }

        @Override
        public int getItemCount() {
            return memberList.size();
        }


    }
}
