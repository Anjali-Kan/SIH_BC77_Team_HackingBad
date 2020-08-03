package propya.mr.jeevan.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import propya.mr.jeevan.Activities.LockScreenDumb;
import propya.mr.jeevan.R;

public class LockScreen extends Service {
    public LockScreen() {
    }

    @Override
    public int onStartCommand(Intent i1, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                Log.d(this.getClass().getName(),"this workers");
                Toast.makeText(LockScreen.this, "This is working", Toast.LENGTH_SHORT).show();
                Handler h = new Handler();
                h.postDelayed(() -> {
                    Intent intent = new Intent(context,LockScreenDumb.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }, 500);
            }
        };

        registerReceiver(broadcastReceiver,intentFilter);


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FCMBase base = new FCMBase();
        base.createNotificationChannel(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Helping you during tough times")
                .setContentTitle("You are always safe")
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(6969,builder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
