package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import propya.mr.jeevan.R;

public class AmbulanceRequestACK extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_request);
        String emergencyId = getIntent().getStringExtra("emergencyId");

        FirebaseFirestore.getInstance().collection("emergencies").document(emergencyId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                String assignedAmbulance = (String) documentSnapshot.getString("assignedAmbulance");
                if(assignedAmbulance != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AmbulanceRequestACK.this).setTitle("Patient already found help").setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finishAndRemoveTask();
                            System.exit(0);
                        }
                    });
                    builder.show();
                }else{
                    final HashMap<String, Object> data = new HashMap<>();
                    data.put("assignedAmbulance", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    documentSnapshot.getReference().update(data);
                    LocationServices.getFusedLocationProviderClient(AmbulanceRequestACK.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            GeoPoint location1 = documentSnapshot.getGeoPoint("location");
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse(String.format("http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s",String.valueOf(location.getLatitude()),
                                            String.valueOf(location.getLatitude()),String.valueOf(location1.getLatitude()),
                                            String.valueOf(location1.getLongitude()))));
                            startActivity(intent);
                        }
                    });
                }
            }
        });


    }
}
