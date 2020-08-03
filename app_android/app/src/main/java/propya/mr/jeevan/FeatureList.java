package propya.mr.jeevan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import propya.mr.jeevan.Activities.AfterConfirmation;
import propya.mr.jeevan.Activities.DoctorAppointment;
import propya.mr.jeevan.Activities.DoctorDetails;
import propya.mr.jeevan.Activities.DoctorSearch;
import propya.mr.jeevan.Activities.EmergencyAssignAlert;
import propya.mr.jeevan.Activities.EntryPoints.AmbulanceActivity;
import propya.mr.jeevan.Activities.EntryPoints.DoctorActivity;
import propya.mr.jeevan.Activities.EntryPoints.UserActivity;
import propya.mr.jeevan.Activities.HospitalSearch;
import propya.mr.jeevan.Activities.HospitalsListActivity;
import propya.mr.jeevan.Activities.KnowTheHospital;
import propya.mr.jeevan.Activities.PatientAppointment;
import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.Activities.TelemedicineVideo;
import propya.mr.jeevan.Activities.UserProfile;
import propya.mr.jeevan.Activities.VolunteerHelp;
import propya.mr.jeevan.ChatBot.ChatBotMain;
import propya.mr.jeevan.Helpers.DynamicLinkHelper;
import propya.mr.jeevan.SOS.ChooseEmergencyActivity;
import propya.mr.jeevan.Services.AmbulanceLocation;
import propya.mr.jeevan.Services.LockScreen;
import propya.mr.jeevan.Services.MessageReadServer;

public class FeatureList extends ActivityHelper {//do this extend
    //remove onCreate and return Layot in getView then bindview

    @BindView(R.id.center_linear)
    LinearLayout rootLinear;

    @Override
    protected void viewReady(View v) {
        addFeature(ChooseEmergencyActivity.class);
//        addFeature(KnowTheHospital.class);
        addFeature(SchemeFinder.class);
        addFeature(PatientAppointment.class);
        addFeature(DoctorAppointment.class);
        
        // Driver alert intent
        Intent emergencyIntent = new Intent(this, EmergencyAssignAlert.class);
        emergencyIntent.putExtra("emergencyId", "0n7Pupxyb6OYe6J8q9S5");
        addFeature(EmergencyAssignAlert.class.getSimpleName(), emergencyIntent);


        addFeature(AmbulanceActivity.class);
        addFeature(UserActivity.class);
        addFeature(DoctorActivity.class);

        addFeature(VolunteerHelp.class);
        addFeature(DoctorDetails.class);
        addFeature(HospitalSearch.class);
        addFeature(HospitalsListActivity.class);
        addFeature(ChooseEmergencyActivity.class);
        addFeature(UserProfile.class);
        addFeature(ChatBotMain.class);
        addFeature("Ambulance Timer", abc->{
            ContextCompat.startForegroundService(this,new Intent(this, AmbulanceLocation.class));
        });
        addFeature("Locksceen Timer", abc->{
            ContextCompat.startForegroundService(this,new Intent(this, LockScreen.class));
        });
        addFeature("Sms server",v5-> ContextCompat.startForegroundService(this,new Intent(this, MessageReadServer.class)));


        Intent i = new Intent(FeatureList.this,AfterConfirmation.class);
        Bundle testBundle = new Bundle();
        testBundle.putString("first_aid_type","Gunshot");
        testBundle.putString("first_aid_text",getString(R.string.first_aid_gunshot_text));
        testBundle.putStringArray("first_aid_urls",new String[] {getString(R.string.first_aid_gunshot)});
        i.putExtras(testBundle);
        addFeature("First Aid Check",i);
//        addFeature("GetLoc",v1->{
//            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnCompleteListener(task -> {
//                log("Task complete");
//                log("Task complete "+task.isSuccessful());
//                if(!task.isSuccessful())
//                    log("Task fail "+task.getException().getLocalizedMessage());
//
//            });
//        });

        addFeature(DoctorSearch.class);
    }

    void addFeature(String name, Intent i){
        addFeature(name, v -> startActivity(i));
    }

    void addFeature(String name, View.OnClickListener i){
            Button b = new Button(this);
            b.setText(name);
            b.setOnClickListener(i);
            rootLinear.addView(b);
    }

    void addFeature(Class c){
        addFeature(c.getSimpleName(),new Intent(this,c));
    }


    @Override
    protected int getRootView() {
        return R.layout.generic_centre_linear;
    }
}
