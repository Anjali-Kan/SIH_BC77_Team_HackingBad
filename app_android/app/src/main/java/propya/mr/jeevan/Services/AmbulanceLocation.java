package propya.mr.jeevan.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import propya.mr.jeevan.Constants;
import propya.mr.jeevan.Helpers.LocationHelper;
import propya.mr.jeevan.R;

public class AmbulanceLocation extends Service {
    Location lastLocation=null;

    public AmbulanceLocation() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FCMBase base = new FCMBase();
        base.createNotificationChannel(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Tracking Ambulance")
                .setContentText("Sending your locations to the needy")
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(786,builder.build());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startUpdating();
        return START_STICKY;
    }

    private void startUpdating() {
        if(!canStart())
            stopForeground(true);

        new LocationHelper(this).getLocation(location -> {
            if(lastLocation==null || lastLocation.distanceTo(location)>Constants.refreshAmbulanceMinDistance) {
                HashMap<String, Object> updates = new HashMap<>();
                updates.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
                Log.i("update location","starting");
                FirebaseFirestore.getInstance().collection("ambulance").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .update(updates).addOnCompleteListener(task -> {
                    Log.i("update location",task.toString());
                    Log.i("update location",""+task.isSuccessful());
                    if(!task.isSuccessful())
                        Log.i("update location",""+task.getException().getLocalizedMessage());
                });
                lastLocation = location;
            }else{
                Log.i("update location","skipping update");
            }

            Handler h = new Handler();
            h.postDelayed(this::startUpdating,1000* Constants.refreshAmbulanceLocation);
        });
    }

    private boolean canStart() {
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            return false;

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return false;

        return true;


    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }
}
