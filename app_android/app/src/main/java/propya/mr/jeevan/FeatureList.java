package propya.mr.jeevan;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Constructor;

import butterknife.BindView;
import propya.mr.jeevan.Activities.AfterConfirmation;
import propya.mr.jeevan.Activities.Telemedicine;
import propya.mr.jeevan.Activities.TelemedicineVideo;
import propya.mr.jeevan.Activities.UserProfile;
import propya.mr.jeevan.Helpers.DynamicLinkHelper;
import propya.mr.jeevan.SOS.ChooseEmergencyActivity;
import propya.mr.jeevan.Activities.KnowTheHospital;
import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.Activities.VolunteerHelp;
import propya.mr.jeevan.Services.AmbulanceLocation;

public class FeatureList extends ActivityHelper {//do this extend
    //remove onCreate and return Layot in getView then bindview

    @BindView(R.id.center_linear)
    LinearLayout rootLinear;

    @Override
    protected void viewReady(View v) {
        addFeature(ChooseEmergencyActivity.class);
        addFeature(KnowTheHospital.class);
        addFeature(SchemeFinder.class);
        addFeature(VolunteerHelp.class);
        addFeature(TelemedicineVideo.class);
        addFeature(ChooseEmergencyActivity.class);
        addFeature(UserProfile.class);
        addFeature("Ambulance Timer", abc->{
            ContextCompat.startForegroundService(this,new Intent(this, AmbulanceLocation.class));
        });

        addFeature("Link generate",abc->{
            DynamicLinkHelper helper = new DynamicLinkHelper(FeatureList.this);
            helper.getLink(this::log);
        });

        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            showToast(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }

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
