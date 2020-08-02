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
    private String prevQn = null;

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

        try {
            addSymptom(id,choice);
            getNextQns();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addSymptom(String id,String choice) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("choice_id",choice);
        arraySymptomps.put(jsonObject);
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
                        JSONArray conditions = data.getJSONArray("conditions");
                        JSONObject finalDisease ;
                        if(conditions.length()==0){
                            finalDisease = new JSONObject();
                            finalDisease.put("name","Unknown");
                            finalDisease.put("common_name","We are sorry\nWe could not detect the disease");
                        }else{
                            finalDisease=(JSONObject) conditions.get(0);
                        }

                        callBack.finalDisease(finalDisease);

                    }
                    else
                    {
                        JSONObject question = data.getJSONObject("question");
                        String text = question.getString("text");
                        if(text.equals(prevQn)){
                            data.put("should_stop",true);
                            this.volleyCallBack(data);
                            return;
                        }
                        prevQn = text;
                        callBack.replyFromBot(question);

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
        public void replyFromBot(JSONObject s) throws JSONException;

        public void symptompsDetected(String s);

        public void finalDisease(JSONObject s);
    }

}
