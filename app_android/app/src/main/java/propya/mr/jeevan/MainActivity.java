package propya.mr.jeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import propya.mr.jeevan.Helpers.DynamicLinkHelper;
import propya.mr.jeevan.SOS.ChooseEmergencyActivity;
import propya.mr.jeevan.Activities.HospitalFinderActivity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.Activities.UserProfile;
import propya.mr.jeevan.Services.RegisterTopics;

public class MainActivity extends ActivityHelper {


    protected void viewSetter() {
        ImageView sosButton = (ImageView) findViewById(R.id.sos_button);
        sosButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ChooseEmergencyActivity.class)));
//todo remove this
        Button testerbutt = (Button)findViewById(R.id.tester_butt);
        testerbutt.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, FeatureList.class);
            startActivity(i);
        });
        ImageView hospFinder = (ImageView) findViewById(R.id.hosp_finder_button);
        hospFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HospitalFinderActivity.class);
                startActivity(i);
                //finish();
            }
        });
        ImageView schemeFinder = (ImageView) findViewById(R.id.scheme_finder_button);
        schemeFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SchemeFinder.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void viewReady(View v) {
        alwaysRunNoMatterWhat();
        viewSetter();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_home_screen;
    }

    void login(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(), (data, result) -> {
                        if (result == RESULT_OK) {
                            showToast("Sign in successfull");
                            startActivity(UserProfile.class);

                        } else if (result == RESULT_CANCELED) {
                            showToast("Sign in cancelled");
                            exit();
                        }
                    });
        }
    }

    void alwaysRunNoMatterWhat(){
        login();
        new DynamicLinkHelper(this).getLink(null);
        RegisterTopics.registerTopics();
        startActivity(new Intent(this, FeatureList.class));
    }
}