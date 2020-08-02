package propya.mr.jeevan.Helpers;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import propya.mr.jeevan.Constants;

public class DynamicLinkHelper {
    Context c;

    public DynamicLinkHelper(Context c) {
        this.c = c;
    }

    public String createLink(){

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser==null)
            return null;

        Uri uri = Uri.parse(Constants.URLs.WEB_HELP);
        uri.buildUpon().appendQueryParameter("userId",currentUser.getUid());


        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(uri)
                .setDomainUriPrefix(Constants.URLs.PAGE_LINK)
                .setAndroidParameters(new DynamicLink
                        .AndroidParameters.Builder().
                        setFallbackUrl(uri)
                        .build())
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();
        return dynamicLinkUri.toString();
    }

    void handleIntent(){

    }

}
