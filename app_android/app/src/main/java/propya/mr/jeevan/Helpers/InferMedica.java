package propya.mr.jeevan.Helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import propya.mr.jeevan.Helpers.ApiCallHelpers;

public class InferMedica {


    public static final String[] inferMedicaKeys = {"051367bc","535c24c252642cbd598472527310ebf9"};

    String url = "https://api.infermedica.com/v2/%s";

    Context c;
    RepliesBot callBack;
    JSONArray arraySymptomps;
    String sex;
    int age;
    public JSONObject lastResponse;

    public InferMedica(Context c, RepliesBot callBack,String sex,int age) {
        this.c = c;
        this.callBack = callBack;
        this.sex = sex;
        this.age = age;
    }

    public void sendMessage(String message){
        ApiCallHelpers helpers = new ApiCallHelpers(c);
        HashMap<String,Object> data = new HashMap<>();
        data.put("text",message);
        helpers.callVolley(String.format(url, "parse"), data, new ApiCallHelpers.CallbackVolley() {
            @Override
            public void volleyCallBack(String msg) {
            }

            @Override
            public void volleyCallBack(JSONObject data) {
                try {
                    JSONObject jsonObject =  data.getJSONArray("mentions").getJSONObject(0);
                    callBack.symptompsDetected(jsonObject.getString("common_name"));

                    jsonObject.remove("orth");
                    jsonObject.remove("type");
                    jsonObject.remove("name");
                    jsonObject.remove("common_name");

                    if(arraySymptomps == null){
                        arraySymptomps = new JSONArray();
                        jsonObject.put("initial",true);
                    }
                    arraySymptomps.put(jsonObject);
                    getNextQns();

                    Log.i("inferMedica",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void proceed(String id,String choice){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("choice_id",choice);
            arraySymptomps.put(jsonObject);
            getNextQns();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getNextQns() {
        ApiCallHelpers helpers = new ApiCallHelpers(c);
        HashMap<String,Object> data = new HashMap<>();
        data.put("sex",sex);
        data.put("age",age);
        data.put("evidence",arraySymptomps);
        Log.i("infermedica data",data.toString());
        helpers.callVolley(String.format(url, "diagnosis"), data, new ApiCallHelpers.CallbackVolley() {
            @Override
            public void volleyCallBack(String msg) {
                Log.i("infermedia error",msg);
            }

            @Override
            public void volleyCallBack(JSONObject data) {
                Log.i("infermedia ",data.toString());
                try {
                    lastResponse = data;
                    if (data.getBoolean("should_stop"))
                    {
                        Toast.makeText(c, "Should stop", Toast.LENGTH_SHORT).show();
                        //get the final diaonosis and stop
                        JSONObject finalDisease=(JSONObject) data.getJSONArray("conditions").get(0);
                        finalDisease.get("name");
                        finalDisease.get("common_name");
                        callBack.finalDisease(finalDisease);

                    }
                    else
                    {
                        //confused
                        callBack.replyFromBot(data.getJSONObject("question"));
                        //ask ques and get response

//                        extractQuestionData(data.getJSONObject("question"));
//                        String response;
//                        JSONObject newSymptom = new JSONObject();
//                        newSymptom.put("id","");
//                        newSymptom.put("choice_id","");
//                        arraySymptomps.put(newSymptom);
//                        getNextQns();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
    private JSONObject extractQuestionData(JSONObject question)
    {
        try {
            question.get("text"); //ask this
            question.get("type");//do something with this


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public interface RepliesBot{
        public void replyFromBot(JSONObject s);

        public void symptompsDetected(String s);

        public void finalDisease(JSONObject s);
    }

}
