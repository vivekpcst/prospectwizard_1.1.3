package com.dzo.prospectWizard.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dzo.prospectWizard.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
public static final String PROSPECT_ID="prospect_channel_01";
    private static final String TAG = "MyFirebaseMsgService";
    String statement;
    String Action = "";
    String phone = "";
    String Sms = "";
    String Name = "";
    String message;
    String[] namearray;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

       try {
            JSONObject obj = new JSONObject(remoteMessage.getData().get("message"));
            statement = obj.getString("message");
            namearray = statement.split(",");

           if (namearray.length > 3) {
               Action += namearray[0];
               phone += namearray[1];
               Sms += namearray[2];
               Name += namearray[3];

           }
           else if (namearray.length > 2) {
               Action += namearray[0];
               phone += namearray[1];
               Name += namearray[2];
           }

           message = Action+" to"+ " ("+phone+") ";

        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendNotification(remoteMessage.getData().get("title"), message);

    }
    private void sendNotification(String messageTitle, String messageBody) {
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;



        Intent intent = new Intent(this, ActivityMain.class);
        intent.putExtra("name", statement);
        Log.e("statment",""+statement);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, m /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {500, 500, 500, 500, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        createNotificationChannel();

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this,PROSPECT_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.appico3)
                .setContentTitle("Porspect Wizard")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.BLUE, 1, 1)
                .setSound(defaultSoundUri)
                .setChannelId(PROSPECT_ID)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m/* ID of notification */, notificationBuilder.build());
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            CharSequence name="Prospect Wizard";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel=new NotificationChannel(PROSPECT_ID,name,importance);

            channel.setDescription("Prospect wizard notification");
            NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);


        }



    }
}