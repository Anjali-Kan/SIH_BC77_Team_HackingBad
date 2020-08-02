package propya.mr.jeevan.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Constants;

public class DynamicLinkHelper {
    Context c;


    public DynamicLinkHelper(Context c) {
        this.c = c;
    }

    public void getLink(@Nullable final SelfQR listener){
        Log.d("Dynamic link","requesting link");
        SharedPreferences self_url = c.getSharedPreferences("self_url", Context.MODE_PRIVATE);
        if(self_url.getString("dynamic_shortLink",null) != null){
            if(listener!=null)
                listener.minifiedUrl(self_url.getString("dynamic_shortLink",null));
            return;
        }
        createLink(l->{
            self_url.edit().putString("dynamic_shortLink",l).apply();
            if(listener!=null)
                listener.minifiedUrl(l);
        });
    }

    private void createLink(SelfQR listener){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null){
            listener.minifiedUrl(null);
            return;
        }

        Uri uri = Uri.parse(Constants.URLs.WEB_HELP);
        uri = uri.buildUpon().appendQueryParameter("userId",currentUser.getUid()).build();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(uri)
                .setDomainUriPrefix(Constants.URLs.PAGE_LINK)
                .setAndroidParameters(new DynamicLink
                        .AndroidParameters.Builder().
                        setFallbackUrl(uri)
                        .build())
                .buildShortDynamicLink().addOnCompleteListener(t->{
                    if(!t.isSuccessful()){
                        listener.minifiedUrl(null);
                        Log.d("Dynaminc link","error "+t.getException().getLocalizedMessage());
                        Log.d("Dynaminc link","error "+t.getException().getMessage());
                        t.getException().printStackTrace();
                        return;
                    }
                    listener.minifiedUrl(t.getResult().getShortLink().toString());


        });


    }

    public void handleIntent(ActivityHelper helper){
        handleIntent(helper.getIntent(),helper);
    }

    public void handleIntent(Intent data,LinkParsed callback){
        if(data == null){
            callback.userIdHelp(null);
            return;
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(data).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        PendingDynamicLinkData result = task.getResult();
                        if(result == null){
                            callback.userIdHelp(null);
                            Log.d("Dynamic link error","result null");
                            return;
                        }
                        Uri link = result.getLink();
                        if(link==null){
                            callback.userIdHelp(null);
                            Log.d("Dynamic link error","link uri null");
                            return;
                        }
                        Log.d("Dynamic link ",link.toString());
                        String userId = link.getQueryParameter("userId");
                        callback.userIdHelp(userId);
                    }else{
                        Log.d("Dynamic link error",task.getException().getLocalizedMessage());
                    }
                });

    }

    public interface LinkParsed{
        public void userIdHelp(String uid);
    }
    public interface SelfQR{
        public void minifiedUrl(String link);
    }

    public interface BitmapReady{
        public void generatedBitmap(Bitmap b);
    }

}
