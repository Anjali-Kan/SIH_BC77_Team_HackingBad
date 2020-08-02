package propya.mr.jeevan.Helpers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    Context context;

    public LocationHelper(Context context) {
        this.context = context;
    }


    public void getLocation(SimpleLocationCallBack callBack) {
        getLocation(l -> {
            if(callBack!=null)
                callBack.locationObtained(l.getLatitude(),l.getLongitude());
        });
    }

    @SuppressLint("MissingPermission")
    public void getLocation(LocationCallBack callBack) {
        LocationServices.getFusedLocationProviderClient(context).getLastLocation().addOnSuccessListener(location -> {
            if(callBack!=null){
                callBack.locationObtained(location);
            }
        });
    }


    public interface SimpleLocationCallBack{
        void locationObtained(double lat,double lon);
    }

    public interface LocationCallBack{
        void locationObtained(Location l);
    }

}
