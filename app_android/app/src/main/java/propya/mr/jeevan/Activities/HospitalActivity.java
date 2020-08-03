package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.ChatBot.ChatBotMain;
import propya.mr.jeevan.R;

public class HospitalActivity extends ActivityHelper {
    private Button mChatbot;
    private Button mHospital;
    private Button mDoctor;

    @Override
    protected void viewReady(View v) {
        mChatbot = findViewById(R.id.know_the_problem_btn);
        mChatbot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChatBotMain.class));
            }
        });

        mHospital = findViewById(R.id.find_hosp_btn);
        mHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HospitalSearch.class));
            }
        });

        mDoctor = findViewById(R.id.find_doctor_btn);
        mDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DoctorSearch.class));
            }
        });

    }

    @Override
    protected int getRootView() {
        return R.layout.activity_hospital;
    }
}
