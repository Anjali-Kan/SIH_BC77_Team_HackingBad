package propya.mr.jeevan.Activities.EntryPoints;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import propya.mr.jeevan.Activities.DoctorAppointment;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.R;

public class DoctorActivity extends ActivityHelper {


    @Override
    protected void viewReady(View v) {
        startActivity(DoctorAppointment.class);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_doctor;
    }
}