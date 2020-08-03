package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import propya.mr.jeevan.R;


public class EmergencyAssignAlert extends AppCompatActivity {

    private String mEmergencyId;
//    private TextView mEmergencyIdTextView;
    private Button mProceedBtn;
    private ImageView mImageView;

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;

    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_assign_alert);

        mImageView = findViewById(R.id.iv_flash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.blink);
        mImageView.startAnimation(animation);
        mMediaPlayer = MediaPlayer.create(this, R.raw.beep);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(mCompletionListener);

        mEmergencyId = getIntent().getStringExtra("emergencyId");
        Log.i(getLocalClassName(),mEmergencyId);

//        mEmergencyIdTextView = findViewById(R.id.tv1);
//        mEmergencyIdTextView.setText(mEmergencyId);

        mProceedBtn = findViewById(R.id.btn1);
        mProceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmergencyAssignAlert.this, EmergencyRideDetails.class);
                intent.putExtra("emergencyId", mEmergencyId);
                releaseMediaPlayer();
                startActivity(intent);
                finish();
            }
        });
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }
}
