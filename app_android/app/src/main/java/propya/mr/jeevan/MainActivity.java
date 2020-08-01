package propya.mr.jeevan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import propya.mr.jeevan.Services.RegisterTopics;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterTopics.registerTopics();
    }
}
