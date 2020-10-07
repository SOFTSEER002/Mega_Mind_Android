package com.doozycod.megamind.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.doozycod.megamind.Activities.MainNavigation;
import com.doozycod.megamind.Utils.JobScheduleUtils;
import com.doozycod.megamind.Utils.SharedPreferenceMethod;

import java.text.SimpleDateFormat;
import java.util.Date;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class AppService extends JobService {
    SharedPreferenceMethod sharedPreferenceMethod;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), MainNavigation.class);
        getApplicationContext().startService(service);


        sharedPreferenceMethod = new SharedPreferenceMethod(getApplicationContext());
        Date d = new Date();
        if (isNetworkAvailable()) {
            if (sharedPreferenceMethod.getAssignmentDay().equals(checkAssignmentDay()) && sharedPreferenceMethod.getDate() == +d.getHours()) {
                show_Notification();
            } else {
                Log.e(TAG, "onStartJob: Notification will not show!");
            }
        } else {
            if (sharedPreferenceMethod.getDate() == 23) {
                sharedPreferenceMethod.saveAssignmentTime(0);
            } else {
                sharedPreferenceMethod.saveAssignmentTime(sharedPreferenceMethod.getDate() + 1);
            }
        }
        JobScheduleUtils.scheduleJob(getApplicationContext()); // reschedule the job
        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public String checkAssignmentDay() {

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();

        String dayOfTheWeek = sdf.format(d);

        Log.e(TAG, "checkAssignmentDay: " + dayOfTheWeek + "  " + d.getHours());
        return dayOfTheWeek;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void show_Notification() {

        Intent intent = new Intent(getApplicationContext(), MainNavigation.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        bundle.putString("assignmentReceived", "assignmentReceived");
        String CHANNEL_ID = "MegMindChannel";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_DEFAULT);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("New Assignment is here!")
                .setContentText("Please check Assignment section before submission date!")
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.sym_action_chat, "Open", pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .setAutoCancel(true)
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1, notification);

    }

}
