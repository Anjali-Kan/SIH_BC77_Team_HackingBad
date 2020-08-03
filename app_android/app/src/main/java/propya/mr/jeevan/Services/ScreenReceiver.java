package propya.mr.jeevan.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import propya.mr.jeevan.SOS.ChooseEmergencyActivity;

public class ScreenReceiver extends BroadcastReceiver {

    public static boolean wasScreenOn = true;
    public static int counter = 0;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.e("LOB","onReceive");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // do whatever you need to do here
            wasScreenOn = false;
            Log.e("LOB","wasScreenOn"+wasScreenOn);
            counter++;
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // and do whatever you need to do here
            wasScreenOn = true;
            counter++;
        }
//       reference - https://stackoverflow.com/questions/30029978/how-to-detect-device-power-button-press-twice-in-android-programmatically
        if(counter==4) {
            //counter = 0;
            Intent i = new Intent(context, ChooseEmergencyActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("isEmergency",true);
            context.startActivity(i);
        }
    }
}
