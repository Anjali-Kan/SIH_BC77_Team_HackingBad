package propya.mr.jeevan.Activities.EntryPoints;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import propya.mr.jeevan.Activities.HospitalActivity;
import propya.mr.jeevan.Activities.SchemeFinder;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Services.LockService;
import propya.mr.jeevan.R;
import propya.mr.jeevan.SOS.ChooseEmergencyActivity;

public class UserActivity extends ActivityHelper {


    @Override
    protected void viewReady(View v) {
        viewSetter();
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_home_screen;
    }

    protected void viewSetter() {
        ImageView sosButton = (ImageView) findViewById(R.id.sos_button);
        sosButton.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, ChooseEmergencyActivity.class)));
//TODO remove this
        ImageView hospFinder = (ImageView) findViewById(R.id.hosp_finder_button);
        hospFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, HospitalActivity.class);
                startActivity(i);
                //finish();
            }
        });
        ImageView schemeFinder = (ImageView) findViewById(R.id.scheme_finder_button);
        schemeFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserActivity.this, SchemeFinder.class);
                startActivity(i);
            }
        });
//        startService(new Intent(getApplicationContext(), LockService.class));
    }
}