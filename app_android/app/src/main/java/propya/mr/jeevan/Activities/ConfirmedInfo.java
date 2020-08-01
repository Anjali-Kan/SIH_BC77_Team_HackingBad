package propya.mr.jeevan.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import propya.mr.jeevan.Helpers.NearbyPlaces;
import propya.mr.jeevan.R;

public class ConfirmedInfo extends AppCompatActivity {

    TextView hospital_status,ambulance_status, first_aid;
    String documentID;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_confirmed_info);
        hospital_status=findViewById(R.id.hospital_status_text);
        ambulance_status=findViewById(R.id.ambulance_status_text);
        first_aid=findViewById(R.id.first_aid_status_text);

        db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("emergencies").document(documentID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("ConfirmedInfo", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("ConfirmedInfo", "Current data: " + snapshot.getData());
                    setInfo(snapshot);
                } else {
                    Log.d("ConfirmedInfo", "Current data: null");
                }
            }
        });
    }

    private void setInfo(DocumentSnapshot snapshot) {

        final Location myLocation= new NearbyPlaces().getCurrentLocation(ConfirmedInfo.this);

        if(snapshot.getString("assignedHospital")!=null)
        {
            db.collection("hospitals").document(snapshot.getString("assignedHospital")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    GeoPoint hospitalLocation=task.getResult().getGeoPoint("location");
                    Location con = new Location(myLocation);
                    float distanceTo=1000000;
                    if (hospitalLocation != null) {
                        con.setLatitude(hospitalLocation.getLatitude());
                        con.setLongitude(hospitalLocation.getLongitude());
                        distanceTo = con.distanceTo(myLocation);
                    }
                    hospital_status.setText(task.getResult().getString("name")+" hospital is "+distanceTo/1000+" km. away.");

                }
            });

        }
        else
        {
            hospital_status.setText("Waiting For Assignment...");
        }

        if(snapshot.getString("assignedAmbulance")!=null)
        {
            ambulance_status.setText("Mar jao baba "+snapshot.getString("assignedAmbulance"));
        }
        else
        {
            ambulance_status.setText("Waiting For Assignment...");
        }

        first_aid.setText("");



    }


}
