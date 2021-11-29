package com.example.loginandsignup;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class
NotificationHelper extends Application {
    public static final String channel_ID = "channel";
//    public static final String channelName = "Channel";

//    private NotificationManager mManager;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(
                    channel_ID,
                    "channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("This is channel!!!!");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

//    public NotificationHelper(Context base) {
//        super(base);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            createChannels();
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void createChannels() {
//        NotificationChannel channel = new NotificationChannel(channel_ID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
//        channel.enableLights(true);
//        channel.enableVibration(true);
//        channel.setLightColor(R.color.design_default_color_primary);
//        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//
//        getManager().createNotificationChannel(channel);
//    }
//
//    public NotificationManager getManager(){
//        if(mManager == null){
//            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        }
//        return mManager;
//    }

//    public NotificationCompat.Builder getChannelNotification(String title, String message){
//        return new NotificationCompat.Builder(getApplicationContext(), channelID)
//                .setContentTitle(title)
//                .setContentText(message);
//    }
}
