package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import propya.mr.jeevan.Helpers.DynamicLinkHelper;
import propya.mr.jeevan.R;

public class LockScreenDumb extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
            );
        setContentView(R.layout.activity_lock_screen_dumb);
        Log.d(this.getLocalClassName(),"started activity");
        ImageView imageView = findViewById(R.id.qrCodeHolder);

        new DynamicLinkHelper(this).getQrCode(b->{
            if (b==null){
                finish();
                return;
            }
            imageView.setImageBitmap(b);
        });
    }
}