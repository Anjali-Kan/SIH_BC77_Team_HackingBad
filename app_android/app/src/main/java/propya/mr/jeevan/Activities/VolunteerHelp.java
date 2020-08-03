package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.Helpers.ApiCallHelpers;
import propya.mr.jeevan.R;

public class VolunteerHelp extends ActivityHelper {


    @Override
    protected void viewReady(View v) {
        showAlert(getResources().getStringArray(R.array.volunteer_message),"Proceed","Opt Out",
                (accept,dialog)->{
                    if(accept){
                        dialog.dismiss();
                        startFetch();
                    }else {
                        exit();
                    }
                });
    }

    void startFetch(){
        startProgress(null);
        String emergencyId = dataIntent.getStringExtra("emergencyId");
        if(emergencyId == null)
            emergencyId = "Model";
        String uid = "defaultUID";
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser!=null)
            uid = currentUser.getUid();
        ApiCallHelpers callHelpers = new ApiCallHelpers(this);
        HashMap<String,Object> params = new HashMap<>();
        params.put("emergencyId",emergencyId);
        params.put("userID",uid);
        callHelpers.callVolley(Constants.URLs.VOLUNTEER_ACK, params, new ApiCallHelpers.CallbackVolley() {
            @Override
            public void volleyCallBack(String msg) {
                log(msg);
            }

            @Override
            public void volleyCallBack(JSONObject data) {
                stopProgress();
                try {
                    log(data.toString());
                    if(data.getString("status").equals("success")){
                        boolean shouldGo = data.getBoolean("shouldGo");
                        int id = R.array.volunteer_res_negative;
                        if(shouldGo)
                            id = R.array.volunteer_res_positive;
                        String[] msgs = getResources().getStringArray(id);
                        showDialog(msgs,shouldGo,data);

                    }else{
                        throw new JSONException("");
                    }
                } catch (JSONException e){}
            }
        });

    }

    private void showDialog(String[] msgs, boolean shouldGo,JSONObject data) {
        showAlert(msgs,getString(R.string.dialog_accept),(accept,dialog)->{
            dialog.dismiss();
            if(shouldGo){
                try {
                    double lat = data.getDouble("lat");
                    double lon = data.getDouble("long");
                    startActivity(String.format(Constants.NearbyPlacesConstants.MAPS_INTENT_MARKER,String.valueOf(lat),String.valueOf(lon)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                exit();
            }
        });
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_volunteer_help;
    }
}
