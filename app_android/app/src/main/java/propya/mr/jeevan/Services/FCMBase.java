package propya.mr.jeevan.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import propya.mr.jeevan.Activities.AmbulanceRequestACK;
import propya.mr.jeevan.Activities.Telemedicine;
import propya.mr.jeevan.Activities.VolunteerHelp;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.R;

public class FCMBase extends FirebaseMessagingService {

    float maxMeters = 1000*5;

    String[] _r = new String[]{"requestVolunteer","requestAmbulance","ambulanceUpdate","telemedicine"};
    ArrayList<String> requestCodes = new ArrayList<>(Arrays.asList(_r));


    void callVolunteers(Map<String, String> data){
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(location -> {
            double lat = Double.parseDouble(data.get("lat"));
            double lon = Double.parseDouble(data.get("lon"));
            Location ref = new Location(location);
            ref.setLatitude(lat);
            ref.setLongitude(lon);
            float v = location.distanceTo(ref);
            if(v > maxMeters)
                return;
            acceptData(data);
        });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("fcm emergency","message received");
        final Map<String, String> data = remoteMessage.getData();
        if(data.containsKey("type")){
            if(!requestCodes.contains(data.get("type")))
                return;
            switch (requestCodes.indexOf(data.get("type"))){
                case 0://requestVolunteer
                    callVolunteers(data);
                    break;
                case 1://requestAmbulance
                    callAmbulance(data);
                    break;
                case 2://ambulanceUpdate
                    break;
                case 3://telemedicine
                    showTelemedicine(data);
                    break;

            }
        }

    }

    private void showTelemedicine(Map<String,String> data) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("TeleMedicine needed")
                .setContentText("User needs help suffering from "+data.get("case"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent intent = new Intent(this, Telemedicine.class);
        intent.putExtra("emergencyId",data.get("emergencyId"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0,"Accept",pendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(66,builder.build());

    }

    private void callAmbulance(Map<String, String> data) {
        ////TODO add your activity here pranav
        String emergencyId = data.get("emergencyId");
        String caseType = data.getOrDefault("case","unknown");
        Double lat = Double.parseDouble(data.get("lat"));
        Double lon = Double.parseDouble(data.get("lon"));
        FirebaseMessaging.getInstance().subscribeToTopic(emergencyId);

    }

    private void acceptData(Map<String, String> data) {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Help needed")
                .setContentText("User needs help suffering from "+data.get("case"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        Intent intent = new Intent(this, VolunteerHelp.class);
        intent.putExtra("emergencyId",data.get("emergencyId"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(0,"Accept",pendingIntent);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(6,builder.build());

    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    private void createNotificationChannel() {
        createNotificationChannel(this);
    }
    public void createNotificationChannel(Context c) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Main";
            String description = "All notis";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("main", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = c.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
