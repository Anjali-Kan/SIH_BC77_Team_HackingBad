package propya.mr.jeevan.Activities.EntryPoints;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.HashMap;

import butterknife.BindView;
import propya.mr.jeevan.Activities.EmergencyAssignAlert;
import propya.mr.jeevan.Activities.EmergencyRideDetails;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.R;

public class AmbulanceActivity extends ActivityHelper {

    @BindView(R.id.viewPatient)
    Button patientAssigned;

    @BindView(R.id.isAvailSwitch)
    Switch isAvail;

    @BindView(R.id.typeSpinner)
    Spinner ambulanceType;

    ArrayAdapter<String> adapter;

    @BindView(R.id.ambulanceNo)
    TextView ambulanceNo;

    @BindView(R.id.patientAvailable)
    TextView patientAvailable;

    boolean hasPatient;



    @Override
    protected void viewReady(View v) {
//        Constants.refreshAmbulanceLocation = 1;
//        Constants.refreshAmbulanceMinDistance = -1;

        patientAssigned.setVisibility(View.GONE);
        startProgress(null);
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll(getResources().getStringArray(R.array.ambulanceType));
        ambulanceType.setAdapter(adapter);
        subscribeData();

        isAvail.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(hasPatient){
                isAvail.setChecked(false);
                return;
            }
            HashMap<String,Object> updates = new HashMap<>();
            updates.put("isAvailable",isChecked);
            FirebaseFirestore.getInstance().collection("ambulance")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .update(updates);

        });

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_ambulance;
    }

    void subscribeData(){
        FirebaseFirestore.getInstance().collection("ambulance").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener((documentSnapshot, e) -> {
                    if(documentSnapshot==null)
                        return;

                    ambulanceNo.setText(documentSnapshot.getString("number"));
                    boolean isAvailable = documentSnapshot.getBoolean("isAvailable");
                    isAvail.setChecked(isAvailable);
                    if(!isAvailable){
                        getEmergency();
                    }else{
                        noPatient();
                    }

                });



    }

    void noPatient(){
        patientAvailable.setText(R.string.noPatient);
        isAvail.setEnabled(true);
        stopProgress();
        patientAssigned.setVisibility(View.GONE);
    }

    void getEmergency(){
        FirebaseFirestore.getInstance().collection("emergencies").whereEqualTo("ambulanceID",FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get(Source.SERVER).addOnSuccessListener(querySnapshot -> {
                    stopProgress();
                    if(querySnapshot.isEmpty()){
                        noPatient();
                        return;
                    }
                    hasPatient(querySnapshot.getDocumentChanges().get(0).getDocument());
                });


    }

    private void hasPatient(QueryDocumentSnapshot id) {
        hasPatient = true;
        stopProgress();
        isAvail.setEnabled(false);
        patientAvailable.setText(id.getString("patientName"));
        patientAssigned.setVisibility(View.VISIBLE);
        patientAssigned.setOnClickListener(v->{
            Intent intent = new Intent(this, EmergencyRideDetails.class);
            intent.putExtra("emergencyId", id.getId());
            startActivity(intent);
        });
    }


}