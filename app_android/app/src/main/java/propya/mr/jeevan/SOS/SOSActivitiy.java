package propya.mr.jeevan.SOS;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;

import propya.mr.jeevan.Activities.ConfirmedInfo;
import propya.mr.jeevan.Helpers.LocationHelper;
import propya.mr.jeevan.R;

public class SOSActivitiy extends AppCompatActivity {

    private String TAG = "SOSActivity";
    boolean[] details = {false, true, true};
    ImageView confirm;
    String uidPatient;
    String emergencyType;
    int[] radioButtons =
            {R.id.radioForMeYes, R.id.radioForMeNo,
                    R.id.radioAmbulanceGovt, R.id.radioAmbulancePrivate,
                    R.id.radioHospitalGovt, R.id.radioHospitalPrivate
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosactivitiy);

        Intent intent = getIntent();
        if (intent.hasExtra("uidPatient"))
            uidPatient = intent.getStringExtra("uidPatient");
        String emtype = intent.getExtras().getString("type");
        if (emtype != null) {
            emergencyType = emtype;
            details[0] = false;
        }

        viewSetter();
        confirm = findViewById(R.id.confirm_sos_details);
        confirm.setOnClickListener(v -> raiseEmergency());
        if(intent.getBooleanExtra("isEmergency",false))
            raiseEmergency();

    }

    private void viewSetter() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = -1;
                for (index = 0; index < radioButtons.length; index++) {
                    if (radioButtons[index] == v.getId())
                        break;
                }
                boolean result = false;

                if (index % 2 == 0) { /// 1st option
                    result = true;
                    ((RadioButton) findViewById(radioButtons[index + 1])).setChecked(false);
                } else {
                    ((RadioButton) findViewById(radioButtons[index - 1])).setChecked(false);
                }
                details[index / 2] = result;
            }
        };
        for (int i : radioButtons) {
            findViewById(i).setOnClickListener(listener);
        }

    }

    private String getPatientID() {
        ////todo - fetch patient id for whom sos has been raised (user is SOSing for someone else)
        if(uidPatient!=null)
            return uidPatient;
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    void raiseEmergency() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        new LocationHelper(this).getLocation(location -> {
            Log.i(TAG, "Location success");
            String null_string = null;
            HashMap<String, Object> data = new HashMap<>();
            data.put("ambulanceID", null_string);
            data.put("assignedHospital", null_string);
            data.put("forSelf", details[0]);
            data.put("govHospital", details[2]);
            data.put("govAmbulance", details[1]);
            data.put("hospitalLocation", new GeoPoint(0, 0));
            data.put("inProgress", true);
            data.put("location", new GeoPoint(location.getLatitude(), location.getLongitude()));
            data.put("patientID", getPatientID());
            Log.i(TAG, "Data successful");
//            if (!details[0]) {
//                data.put("patientID", getPatientID());
//            }
            data.put("raisedBy", FirebaseAuth.getInstance().getCurrentUser().getUid());
            data.put("timestamp", FieldValue.serverTimestamp());
            data.put("type", emergencyType);
            Integer i = -2;
            data.put("toHospital", 13);
            data.put("toPick", 7);

            ArrayList<String> volunteers = new ArrayList<>();
            data.put("volunteer", volunteers);

            FirebaseFirestore.getInstance().collection("emergencies").add(data).addOnSuccessListener(documentReference -> {
                String id = documentReference.getId();//emergency Id
                Intent intent = new Intent(SOSActivitiy.this, ConfirmedInfo.class);
                intent.putExtra("docID", id);
                startActivity(intent);

            });
        });


    }

//    public void submitSOS(View view) {
//        raiseEmergency();
//    }
}
