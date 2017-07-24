package com.practice.android.moments.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.practice.android.moments.Activities.BottomNavigation;
import com.practice.android.moments.R;

import org.json.JSONObject;

/**
 * Created by Ashutosh on 7/18/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getData() + "");

        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            String str_type = jsonObject.getString("type");

            if (str_type.equals("")) {
                String sender_name = jsonObject.getString("SENDER_NAME");
                String Msg = jsonObject.getString("MSG");
                String s_id = jsonObject.getString("SENDER_ID");
                String R_id = jsonObject.getString("RECEPIENT_ID");
                String pic = jsonObject.getString("PIC");
                notificationmessage(Msg, str_type, R_id, s_id, sender_name, pic);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void notificationmessage(String str_message, String str_type, String recepient_id, String str_senderid, String sender_name, String pic) {
        PendingIntent pendingIntent = null;


        if (str_type.equals("like")) {


            Intent intent = new Intent(this, BottomNavigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("LIKE", true);
            intent.putExtra("PIC", pic);
            intent.putExtra("SENDER_NAME", sender_name);
            intent.putExtra("RECEPIENT_ID", recepient_id);
            intent.putExtra("SENDER_ID", str_senderid);
            intent.putExtra("MSG", str_message);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Moments")
                .setContentText(str_message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;

        Log.e("ComponentName", cn.getClassName() + "");


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(6, notificationBuilder.build());
    }


}
