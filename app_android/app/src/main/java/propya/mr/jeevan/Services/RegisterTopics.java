package propya.mr.jeevan.Services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegisterTopics {

    static final String[] topics = {"volunteer","telemedicine"};

    public static void registerTopics(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
            FirebaseMessaging.getInstance().subscribeToTopic(currentUser.getUid());
        for(String s:topics){
            FirebaseMessaging.getInstance().subscribeToTopic(s).addOnCompleteListener(task -> Log.i("fcm","registered topic"));
        }

    }

}
