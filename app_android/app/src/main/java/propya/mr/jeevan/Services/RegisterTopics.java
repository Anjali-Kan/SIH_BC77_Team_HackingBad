package propya.mr.jeevan.Services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterTopics {

    static final String[] topics = {"volunteer"};

    public static void registerTopics(){

        for(String s:topics){
            FirebaseMessaging.getInstance().subscribeToTopic(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("fcm","registered topic");
                }
            });
        }

    }

}
