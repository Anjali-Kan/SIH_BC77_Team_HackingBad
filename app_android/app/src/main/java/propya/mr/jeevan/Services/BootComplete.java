package propya.mr.jeevan.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import propya.mr.jeevan.Constants;

public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        run(context);
    }

    public static void run(Context context){
        ContextCompat.startForegroundService(context,new Intent(context,AmbulanceLocation.class));
        if(Constants.showLockScreen)
            ContextCompat.startForegroundService(context,new Intent(context,LockScreen.class));
        else
            ContextCompat.startForegroundService(context,new Intent(context,LockService.class));

    }

}
