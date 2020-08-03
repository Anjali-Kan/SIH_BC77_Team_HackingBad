package propya.mr.jeevan.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import propya.mr.jeevan.Services.LockScreen;

public class LocationHelper {

    Context context;

    public LocationHelper(Context context) {
        this.context = context;
    }


    public void getLocation(SimpleLocationCallBack callBack) {
        getLocation(l -> {
            if (callBack != null)
                callBack.locationObtained(l.getLatitude(), l.getLongitude());
        });
    }

    @SuppressLint("MissingPermission")
    public void getLocation(LocationCallBack callBack) {
        LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(location -> {
            if (callBack != null) {
                if(location == null)
                    requestUpdates(callBack);
                else
                    callBack.locationObtained(location);
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void requestUpdates(LocationCallBack callback) {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setNumUpdates(5);

        LocationCallback locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                callback.locationObtained(locationResult.getLastLocation());
                LocationServices.getFusedLocationProviderClient(context).removeLocationUpdates(this);
            }
        };
        LocationServices.getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }


    public interface SimpleLocationCallBack{
        void locationObtained(double lat,double lon);
    }

    public interface LocationCallBack{
        void locationObtained(Location l);
    }

}
