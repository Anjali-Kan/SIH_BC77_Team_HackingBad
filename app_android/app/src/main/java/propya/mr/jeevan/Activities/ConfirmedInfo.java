package propya.mr.jeevan.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.Helpers.NearbyPlaces;
import propya.mr.jeevan.Helpers.SearchAnything;
import propya.mr.jeevan.R;

import static android.content.ContentValues.TAG;

public class ConfirmedInfo extends ActivityHelper {

    TextView hospital_status,ambulance_status, first_aid;
    String documentID;
    FirebaseFirestore db;
    String token = null;

    @Override
    protected void viewReady(View v) {
        hospital_status=findViewById(R.id.hospital_status_text);
        ambulance_status=findViewById(R.id.ambulance_status_text);
        first_aid=findViewById(R.id.first_aid_status_text);
        documentID = getIntent().getStringExtra("docID");
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

    @Override
    protected int getRootView() {
        return R.layout.activtiy_confirmed_info;
    }

    private void setInfo(DocumentSnapshot snapshot) {
        if(snapshot.contains("teleToken")&&snapshot.contains("telemedicine")){
            if(token==null){
                token=snapshot.getString("teleToken");
                initializeTele(snapshot.getId(),token);
            }
        }

        if(snapshot.getString("assignedHospital")!=null)
        {
            db.collection("hospitals").document(snapshot.getString("assignedHospital")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull final Task<DocumentSnapshot> task) {
                    LocationServices.getFusedLocationProviderClient(ConfirmedInfo.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            GeoPoint hospitalLocation=task.getResult().getGeoPoint("location");
                            Location con = new Location(location);
                            float distanceTo=1000000;
                            if (hospitalLocation != null) {
                                con.setLatitude(hospitalLocation.getLatitude());
                                con.setLongitude(hospitalLocation.getLongitude());
                                distanceTo = con.distanceTo(location);
                            }
                            hospital_status.setText(task.getResult().getString("name")+" hospital is "+distanceTo/1000+" km. away.");

                        }
                    });

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

        SearchAnything searchAnything = new SearchAnything();
        Query query= searchAnything.getQuery("first aid",null,"name",snapshot.getString("type"),"eq");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                     first_aid.setText(""+document.getString("text"));
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }
            }
        });




    }

    private void initializeTele(String id, String token) {

        showAlert(new String[] {"A doctor has accepted your call!","Click Okay to go to telemedicine video call!"},getString(R.string.dialog_accept),(accept,dialog)->{
            dialog.dismiss();
            Intent i = new Intent(ConfirmedInfo.this, TelemedicineVideo.class);
            i.putExtra("emergencyId",id);
            i.putExtra("teleToken",token);
            startActivity(i);
        });

    }


}
