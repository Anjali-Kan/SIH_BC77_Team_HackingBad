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

import propya.mr.jeevan.SOS.ChooseEmergencyActivity;
import propya.mr.jeevan.Activities.HospitalFinderActivity;
import java.util.Arrays;
import java.util.List;

import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.Activities.UserProfile;
import propya.mr.jeevan.Services.RegisterTopics;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_SCREEN_TIME_OUT = 2000;
    private static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        RegisterTopics.registerTopics();
        ImageView sosButton = (ImageView) findViewById(R.id.sos_button);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChooseEmergencyActivity.class));
            }
        });
//todo remove this
        Button testerbutt = (Button)findViewById(R.id.tester_butt);
        testerbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FeatureList.class);
                startActivity(i);
            }
        });
        ImageView hospFinder = (ImageView) findViewById(R.id.hosp_finder_button);
//        startActivity(new Intent(this, FeatureList.class));
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


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Sign in successfull", Toast.LENGTH_SHORT);
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT);
                finish();
            }
        }
    }
}