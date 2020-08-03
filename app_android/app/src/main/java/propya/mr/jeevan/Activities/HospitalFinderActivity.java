package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import propya.mr.jeevan.ChatBot.ChatBotMain;
import propya.mr.jeevan.R;

public class HospitalFinderActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_finder);
    }

    public void chatBot(View view) {
        startActivity(new Intent(this, ChatBotMain.class));
    }
    public void knowHospital(View view) {
        startActivity(new Intent(this, KnowTheHospital.class));
    }
    public void savedHospital(View view) {

//        startActivity(new Intent(this, ChatBotMain.class));
        Toast.makeText(this,"To be done",Toast.LENGTH_SHORT).show();
        //TODO saving hospital
    }
}
