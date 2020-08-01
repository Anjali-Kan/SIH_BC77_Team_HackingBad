package propya.mr.jeevan;

import android.content.Context;

public class Constants {
    public class URLs
    {
        public static final String NEARBY_PLACES="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    }
    public class NearbyPlacesConstants
    {

        //TODO KEYS
        public static final String PLACESK="some_key";
    }

    public static String getUserType(Context c){
        return "driver";
    }

}
