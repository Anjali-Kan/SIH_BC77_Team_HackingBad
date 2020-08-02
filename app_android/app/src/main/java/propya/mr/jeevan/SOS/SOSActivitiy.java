package propya.mr.jeevan.SOS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;

import propya.mr.jeevan.Activities.ConfirmedInfo;
import propya.mr.jeevan.R;

public class SOSActivitiy extends AppCompatActivity {

    boolean[] details = {false,true,true};
    ImageView confirm ;
    String emergencyType = "gunshot";
    int[] radioButtons =
            {R.id.radioForMeYes,R.id.radioForMeNo,
                    R.id.radioAmbulanceGovt,R.id.radioAmbulancePrivate,
                    R.id.radioHospitalGovt,R.id.radioHospitalPrivate
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosactivitiy);

        viewSetter();
        confirm = findViewById(R.id.confirm_sos_details);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                raiseEmergency();
            }
        });


    }

    private void viewSetter() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = -1;
                for(index=0;index<radioButtons.length;index++){
                    if(radioButtons[index]== v.getId())
                        break;
                }
                boolean result = false;

                if(index%2 == 0){ /// 1st option
                    result = true;
                    ((RadioButton)findViewById(radioButtons[index+1])).setChecked(false);
                }else{
                    ((RadioButton)findViewById(radioButtons[index-1])).setChecked(false);
                }
                details[index/2] = result;
            }
        };
        for(int i : radioButtons){
            findViewById(i).setOnClickListener(listener);
        }

    }


    void getEmegencyType(){

    }

    void raiseEmergency(){
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                HashMap<String,Object> data = new HashMap<>();
                data.put("forSelf",details[0]);
                data.put("type",emergencyType);
                data.put("location", new GeoPoint(location.getLatitude(),location.getLongitude()));
                data.put("raisedBy",FirebaseAuth.getInstance().getCurrentUser().getUid());
                data.put("timestamp", FieldValue.serverTimestamp());
                data.put("govHospital", details[2]);
                data.put("govAmbulance", details[1]);

                FirebaseFirestore.getInstance().collection("emergencies").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        Intent intent = new Intent(SOSActivitiy.this, ConfirmedInfo.class);
                        intent.putExtra("docID",id);
                        startActivity(intent);

                    }
                });
            }
        });


    }


    public void submitSOS(View view) {
        raiseEmergency();
    }
}
