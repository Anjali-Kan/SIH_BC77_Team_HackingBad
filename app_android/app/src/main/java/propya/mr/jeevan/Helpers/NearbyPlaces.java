package propya.mr.jeevan.Helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import propya.mr.jeevan.BluePrint.OnePlace;
import propya.mr.jeevan.Constants;
public class NearbyPlaces {
    private Context c;
    private OnePlace[] safeplaces;
    private ResultReady resultReady;
    private Location locationreturn;


    public NearbyPlaces()
    {

    }
    public NearbyPlaces(Context context,ResultReady resultReady)
    {
        this.c = context;
        this.resultReady = resultReady;
    }
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=42.3675294,-71.186966&rankby=distance&keyword=atm|hospital|police|railway|station&key=AIzaSyCseULsIncv9KI9csnt5sEM1OrSIt5iOs
    private static String msgg;
    private String[] urlFormat= {Constants.URLs.NEARBY_PLACES,"&location=","&rankby=distance","&keyword=","&key="+Constants.NearbyPlacesConstants.PLACESK};
    public void findNearbyPlaces(double latitude, double longitude,String keywords)
    {
        //multiple keywords must be in this format: "atm|hospital|police|railway|station"
        if(keywords==null)
        {
            keywords="hospital";
        }
        String url =urlFormat[0];
        url+=urlFormat[1]+latitude+","+longitude;
        url+=urlFormat[2];
        url+=urlFormat[3]+keywords;
        url+=urlFormat[4];
        ApiCallHelpers helpers = new ApiCallHelpers(c);
        helpers.callVolley(url, new HashMap<String, Object>(), new ApiCallHelpers.CallbackVolley() {
            @Override
            public void volleyCallBack(String msg) {
                Log.i(ApiCallHelpers.class.getName(),"error msg :"+msg);
                msgg=msg;
            }

            @Override
            public void volleyCallBack(JSONObject data) {
                Log.i(ApiCallHelpers.class.getName(),"Response");
                Log.i(ApiCallHelpers.class.getName(),data.toString());
                msgg=data.toString();
                try {
                    if(data.getString("status").equals("OK"))
                    {
                        JSONArray results = data.getJSONArray("results");
                        OnePlace[] safeplaceresults = new OnePlace[results.length()];
                        for(int i=0;i<results.length();i++)
                        {
                            OnePlace curPlace = new OnePlace();
                            JSONObject curObj = results.getJSONObject(i);//one result
                            JSONObject location =curObj.getJSONObject("geometry").getJSONObject("location");//geometry
                            curPlace.setLatitude(location.getDouble("lat"));
                            curPlace.setLongitude(location.getDouble("lng"));
                            curPlace.setName(curObj.getString("name"));
                            curPlace.setVicinity(curObj.getString("vicinity"));
                            curPlace.setIconLink(curObj.getString("icon"));
                            safeplaceresults[i]=curPlace;

                        }

                        safeplaces=safeplaceresults;
                        resultReady.results(safeplaceresults);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //HANDLE JSON HERE


            }
        });

    }



    public  String getResponse()
    {
        return msgg;
    }
    public OnePlace[] getSafeplaces() {
        return safeplaces;
    }

    public interface ResultReady{
        public void results(OnePlace[] results);
    }


    public Location getCurrentLocation(Activity activity)
    {

        new LocationHelper(activity).getLocation(location -> {
            if (location != null) {
                locationreturn=location;
            }
        });

        return locationreturn;
    }
}

