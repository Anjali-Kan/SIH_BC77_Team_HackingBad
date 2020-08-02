package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.Helpers.ApiCallHelpers;
import propya.mr.jeevan.R;

public class Telemedicine extends ActivityHelper {


    @Override
    protected void viewReady(View v) {
        final String emergencyId = dataIntent.getStringExtra("emergencyId");
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        HashMap<String,Object> params = new HashMap<>();
        params.put("docId",uid);
        params.put("emergencyId",emergencyId);
        startProgress(new String[]{
                "Fetching patient",
                "Please wait while we fetch the patient details and connect you"
        });
        ApiCallHelpers helpers = new ApiCallHelpers(this);
        helpers.callVolley(Constants.URLs.TELE_ACK, params, new ApiCallHelpers.CallbackVolley() {
            @Override
            public void volleyCallBack(String msg) {
                log(msg);
            }

            @Override
            public void volleyCallBack(JSONObject data) {
                stopProgress();
                if(data==null){
                    showToast("Error occurred");
                    return;
                }
                log(data.toString());
                try {
                    if(data.getString("status").equals("success")){
                        if(data.getBoolean("shouldGo")){
                            connectToTele(emergencyId);
                        }else{
                            showAlert(getResources().getStringArray(R.array.tele_res_negative),
                                    null,
                                    (didAccept, dialog) -> {
                                        dialog.dismiss();
                                        exit();
                                    });
                        }
                    }else{
                        throw new JSONException("");
                    }
                } catch (JSONException e) {
                    showToast("Some error occurred");
                    e.printStackTrace();
                }

            }
        });


    }

    private void connectToTele(String emergencyId) {
        //TODO anje put ur code right here
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_telemedicine;
    }
}
