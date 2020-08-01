package propya.mr.jeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import propya.mr.jeevan.Activities.HospitalFinderActivity;
import java.util.Arrays;
import java.util.List;

import propya.mr.jeevan.Services.RegisterTopics;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        RegisterTopics.registerTopics();
        ImageView hospFinder = (ImageView)findViewById(R.id.hosp_finder_button);
        hospFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HospitalFinderActivity.class);
                startActivity(i);
                //finish();
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    50);
        }

//        InferMedica inferMedica = new InferMedica(this, new InferMedica.RepliesBot() {
//            @Override
//            public void replyFromBot(JSONObject s) {
//
//            }
//
//            @Override
//            public void symptompsDetected(String s) {
//
//            }
//        },"female",10);
//        inferMedica.sendMessage("I have headache");

    }
//        InferMedica inferMedica = new InferMedica(this, new InferMedica.RepliesBot() {
//            @Override
//            public void replyFromBot(JSONObject s) {
//
//            }
//
//            @Override
//            public void symptompsDetected(String s) {
//
//            }
//        },"female",10);
//        inferMedica.sendMessage("I have headache");



    public void startEme(View view){
        startActivity(new Intent(this, ChooseEmergencyActivity.class));
    }
    /*public void hospFinder(View view) {
        startActivity(new Intent(this,Hospital.class));
    }*/



}
