package propya.mr.jeevan;

import android.Manifest;
import android.content.Context;

public class Constants {

    public static  int refreshAmbulanceLocation = 60;
    public static  int refreshAmbulanceMinDistance = 10;

    public static final boolean isTester = true;
    public static final boolean showLockScreen = true;

    public static final String[] permissions =  {
            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.RECEIVE_BOOT_COMPLETED,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,
    };

    public static class URLs
    {
        public static final String NEARBY_PLACES="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        public static final String VOLUNTEER_ACK="https://us-central1-jeevan-b9882.cloudfunctions.net/acknowledementVolunteer";
        public static final String TELE_ACK="https://us-central1-jeevan-b9882.cloudfunctions.net/ackTelemedicine";
        public static final String WEB_HELP="https://jeevan-b9882.web.app/helpNeeded";
        public static final String PAGE_LINK="https://jeevan.page.link";
    }

    public static class AlgoliaConstants{
        public static final String APP_ID="RVX0QK5HY4";
        public static final String API_KEY="f77556e412b39279b4e81298c847fe0c";
        public static final String H_INDEX="hospitals";
        public static final String D_INDEX="doctorsNew";

//        public static final String APP_ID="86LDTYD1VW";
//        public static final String API_KEY="cdbe48b4d0836d30495aad602c7f861d";
//        public static final String H_INDEX="hospitals";
//        public static final String D_INDEX="doctorsNew";
    }

    public static class NearbyPlacesConstants
    {

        //TODO KEYS
        public static final String PLACESK="some_key";
        public static final String MAPS_INTENT_S_D="http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s";
        public static final String MAPS_INTENT_MARKER = "https://www.google.com/maps/search/?api=1&query=%s,%s";
    }

    public static String getUserType(Context c){
        return "driver";
    }

}
