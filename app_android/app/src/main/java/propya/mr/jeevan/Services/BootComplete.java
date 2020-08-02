package propya.mr.jeevan.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ContextCompat.startForegroundService(context,new Intent(context,AmbulanceLocation.class));
    }
}
