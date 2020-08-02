package propya.mr.jeevan;

import android.content.Context;

public class Constants {

    public static final int refreshAmbulanceLocation = 60;
    public static final int refreshAmbulanceMinDistance = 50;

    public static class URLs
    {
        public static final String NEARBY_PLACES="https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        public static final String VOLUNTEER_ACK="https://us-central1-jeevan-b9882.cloudfunctions.net/acknowledementVolunteer";
        public static final String TELE_ACK="https://us-central1-jeevan-b9882.cloudfunctions.net/ackTelemedicine";

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
