package propya.mr.jeevan;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiCallHelpers {

    Context c;

    public ApiCallHelpers(Context c){
        this.c = c;
    }


    public void callVolley(String url, HashMap<String,Object> data,final CallbackVolley callbackVolley){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callbackVolley.volleyCallBack(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley error",error.getLocalizedMessage());
                callbackVolley.volleyCallBack("Error occurred"+error.getLocalizedMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> header = new HashMap<>();
                header.put("App-Id",InferMedica.inferMedicaKeys[0]);
                header.put("App-Key",InferMedica.inferMedicaKeys[1]);
                header.put("Content-Type","application/json");
                return header;
            }
        };
        request
                .setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(c).add(request);
        callbackVolley.volleyCallBack("Queued Request");
    }


    public interface CallbackVolley{
        void volleyCallBack(String msg);
        void volleyCallBack(JSONObject data);

    }

}
