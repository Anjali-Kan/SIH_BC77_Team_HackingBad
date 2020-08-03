package propya.mr.jeevan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import propya.mr.jeevan.Activities.EntryPoints.AmbulanceActivity;
import propya.mr.jeevan.Activities.EntryPoints.DoctorActivity;
import propya.mr.jeevan.Activities.EntryPoints.UserActivity;
import propya.mr.jeevan.Helpers.DynamicLinkHelper;
import propya.mr.jeevan.SOS.ChooseEmergencyActivity;
import propya.mr.jeevan.Activities.HospitalFinderActivity;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.Activities.UserProfile;
import propya.mr.jeevan.Services.BootComplete;
import propya.mr.jeevan.Services.RegisterTopics;

import static propya.mr.jeevan.Constants.isTester;
import static propya.mr.jeevan.Constants.permissions;

public class MainActivity extends ActivityHelper {



    @Override
    protected void viewReady(View v) {
    }


    boolean hasPermissions(){
        for (String s:permissions)
            if(ContextCompat.checkSelfPermission(this,s)!= PackageManager.PERMISSION_GRANTED){
                log(s);
                return  false;
            }
        return true;
    }

    void requestPermissions(){
        showToast("Please grant permissions");
        ActivityCompat.requestPermissions(this,permissions,57);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==57)
            alwaysRunNoMatterWhat();
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected int getRootView() {
        return R.layout.splash_screen;
    }

    boolean login(){
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            showToast("Please Login");
            List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.PhoneBuilder().build());
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(), (data, result) -> {
                        if (result == RESULT_OK) {
                            showToast("Sign in successfull");

                            alwaysRunNoMatterWhat();


                        } else if (result == RESULT_CANCELED) {
                            showToast("Sign in cancelled");
                            exit();
                        }
                    });
        }
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    private void startNextActivity() {
        log("user type "+getUserType());
        redirectActivity(getUserType());
        finish();


    }

    void redirectActivity(int userType){
        switch (userType){
            case -1:
                startActivity(FeatureList.class);
                break;
            case 0:
                startActivity(UserActivity.class);
                break;
            case 1:
                startActivity(DoctorActivity.class);
                break;
            case 2:
                startActivity(AmbulanceActivity.class);
                break;
        }


    }

    int getUserType()
    {
        if(isTester)
            return -1;
        SharedPreferences sharedPreferences = getSharedPreferences("login_details",MODE_PRIVATE);
        int user_type = sharedPreferences.getInt("user_type", -2);
        if (user_type==-2){
            FirebaseFirestore.getInstance().collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get().addOnSuccessListener(documentSnapshot -> {
                        if(documentSnapshot.contains("user_type")){
                            sharedPreferences.edit().putInt("user_type",((int)documentSnapshot.get("user_type"))).apply();
                        }
                    });

            user_type = 0;
        }
        return user_type;

    }
    void alwaysRunNoMatterWhat(){
        if(!hasPermissions()){
            requestPermissions();
            return;
        }
        if(!login())
            return;
        startNextActivity();
        new DynamicLinkHelper(this).getLink(link -> {
            log("Dynamic link is "+link);
        });
        BootComplete.run(this);
        RegisterTopics.registerTopics();
//        startActivity(new Intent(this, FeatureList.class));
//        startActivity(new Intent(this, FeatureList.class));
//        getUserType();
    }

    private void animate() {
        final View myView = findViewById(R.id.animateMe);

            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

            float initialRadius = (float) Math.hypot(cx, cy);

            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, initialRadius);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.VISIBLE);
                    alwaysRunNoMatterWhat();

                }
            });
            anim.start();
    }

    @Override
    void viewAttached() {
        super.viewAttached();
        animate();
    }
}