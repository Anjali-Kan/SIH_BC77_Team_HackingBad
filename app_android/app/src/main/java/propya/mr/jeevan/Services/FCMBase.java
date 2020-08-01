package propya.mr.jeevan.Services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;

public class FCMBase extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("fcm emergency","message received");
        Map<String, String> data = remoteMessage.getData();

        if(data.containsKey("type")){
            if(Objects.equals(data.get("type"), "request")){
                Log.i("fcm emergency",data.toString());
            }
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
