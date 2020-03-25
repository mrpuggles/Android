package com.example.da105_g4_v0.task;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerCountDownTask extends AsyncTask<String, Long, String> {

    private long endTime, currentTime;
    private Timer timer;
    private long countDown;
    private WeakReference<TextView> textViewWeakReference;

    public TimerCountDownTask(Long endTime, Long currentTime) {
        this.endTime = endTime;
        this.currentTime = currentTime;

    }



    public TimerCountDownTask(Long endTime, Long currentTime, TextView textView) {
        this.endTime = endTime;
        this.currentTime = currentTime;
        this.textViewWeakReference = new WeakReference<>(textView);

    }

    @Override
    protected  String doInBackground(String... string) {


        return getTimer(endTime, currentTime);
    }



//    @Override
//    protected void onPostExecute(String time) {
//
//
//       TextView textView = textViewWeakReference.get();
//
//
//        if (isCancelled()) {
//            return;
//        }
//
//        textView.setText(time);
//
//
//    }


    private String getTimer(long endTime, long currentTime) {

//        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        Long currentTimeLong = date.getTime();
        String currentDate = formatter.format(currentTimeLong);
        long interval;
        interval = endTime - currentTime;

        timer = new Timer();
        String c;
        getRemaining(interval);
        timer.scheduleAtFixedRate(tk, 0, 1000);
        int time = (int) countDown / 1000;
        int day = time / 86400;

        int hour = (time / 3600) - (day * 24);

        int min = (time / 60) - ((day * 24 * 60) + (hour * 60));

        int sec = time - ((day * 24 * 60 * 60) + (hour * 60 * 60) + (min * 60));

        c = day + "天" + hour + "小時" + min + "分" + sec + "秒";

        return c;
    }

    public void getRemaining(long interval) {
        countDown = interval;
    }

    TimerTask tk = new TimerTask() {
        @Override
        public void run() {
            if (countDown == 0) {
                tk.cancel();
            } else {
                countDown = countDown - 1000;
            }
        }
    };




}

