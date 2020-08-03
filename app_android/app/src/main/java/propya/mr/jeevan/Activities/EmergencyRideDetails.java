package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

import propya.mr.jeevan.R;

public class EmergencyRideDetails extends AppCompatActivity {

    private TextView tvtext1, tvtext2, tv_hTitle;
    private Button btnPickedUp, btnloc1, btnloc2;
    private FirebaseFirestore dbref;
    private String emergencyId;

    private String hospitalId = "";
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_ride_details);

        emergencyId = getIntent().getStringExtra("emergencyId");


        tvtext1 = findViewById(R.id.tvtext1);
        tv_hTitle = findViewById(R.id.tv_hTitle);
        tvtext2 = findViewById(R.id.tvtext2);
        btnloc1 = findViewById(R.id.btnloc1);
        btnloc2 = findViewById(R.id.btnloc2);

        dbref = FirebaseFirestore.getInstance();
        Log.i(getLocalClassName(), emergencyId);
        dbref.collection("emergencies").document(emergencyId).get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();

            Map<String, Object> data = document.getData();
            Log.i(getLocalClassName(), data.toString());

            hospitalId = data.get("assignedHospital").toString();

            tvtext1.setText(data.get("type").toString());

            GeoPoint pPos = (GeoPoint) data.get("location");
            btnloc1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double lat = pPos.getLatitude();
                    double lon = pPos.getLongitude();
                    Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q="+lat+","+lon);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
            btnloc1.setVisibility(View.VISIBLE);

            GeoPoint hPos = (GeoPoint) data.get("hospitalLocation");
            btnloc2.setOnClickListener(view -> {
                double lat = hPos.getLatitude();
                double lon = hPos.getLongitude();
                Uri gmmIntentUri = Uri.parse("geo:"+lat+","+lon+"?q="+lat+","+lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            });

        });

        btnPickedUp = findViewById(R.id.btn_pickedup);
        btnPickedUp.setOnClickListener(view -> {

            dbref.collection("hospitals").document(hospitalId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    Map<String, Object> data = document.getData();
                    tvtext2.setText(data.get("name").toString());
                    tv_hTitle.setVisibility(View.VISIBLE);
                    tvtext2.setVisibility(View.VISIBLE);

                }
            });

            btnloc1.setVisibility(View.GONE);
            btnloc2.setVisibility(View.VISIBLE);
            btnPickedUp.setVisibility(View.GONE);

            dbref.collection("emergencies").document(emergencyId).update("toPick",-1);
        });
    }
}
