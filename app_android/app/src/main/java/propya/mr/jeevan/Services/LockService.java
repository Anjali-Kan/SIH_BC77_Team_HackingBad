package propya.mr.jeevan.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import propya.mr.jeevan.R;

public class LockService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FCMBase base = new FCMBase();
        base.createNotificationChannel(this);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Helping you during tough times using keys")
                .setContentTitle("You are always safe")
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(556,builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(new ScreenReceiver(), filter);
        return START_STICKY;
    }

}