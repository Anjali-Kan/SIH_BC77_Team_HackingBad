package propya.mr.jeevan.Helpers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InfermedicaInstance  {
    Context c;
    InferMedica instance;
    ResponseInfermedica responseInfermedica;

    InferMedica.RepliesBot repliesBot;

    String instanceId;

    public InfermedicaInstance(Context c,char s,int age,ResponseInfermedica responseInfermedica){
        this.c = c;
        setCallBack();
        if(s=='m' || s=='M')
            this.instance = new InferMedica(c,repliesBot,"male",age);
        else if(s=='f' || s=='F'){
            this.instance = new InferMedica(c,repliesBot,"female",age);
        }else {
            throw new IllegalArgumentException("Invalid gender");
        }
        this.responseInfermedica = responseInfermedica;
    }


    public void ask1stQn(String mainQn){
        instance.sendMessage(mainQn);
    }

    private void setCallBack() {
        repliesBot = new InferMedica.RepliesBot() {

            @Override
            public void replyFromBot(JSONObject jsonRes) throws JSONException {
                String text = jsonRes.getString("text");
                switch (Arrays.asList(new String[]{"single","group_single","group_multiple"}).indexOf(jsonRes.getString("type"))){
                    case 0:
                        String symptomId = jsonRes.getJSONArray("items").getJSONObject(0).getString("id");
                        String question = jsonRes.getJSONArray("items").getJSONObject(0).getString("name");
                        JSONArray jsonArray = jsonRes.getJSONArray("items").getJSONObject(0).getJSONArray("choices");

                        HashMap<String,String> options = new HashMap<>();

                        for(int i=0;i<jsonArray.length();i++)
                            options.put(jsonArray.getJSONObject(i).getString("label"),jsonArray.getJSONObject(i).getString("id"));


                        responseInfermedica.yesNoQns(symptomId,String.format("%s\n%s",text,question),options);
                        break;
                    case 1:
                        JSONArray items = jsonRes.getJSONArray("items");
                        responseInfermedica.singleOptionQn(text,buildSymptomMap(items));
                        break;
                    case 2:
                        JSONArray items2 = jsonRes.getJSONArray("items");
                        responseInfermedica.allowMultipleAns(text,buildSymptomMap(items2));
                        break;
                }
            }

            private HashMap<String,String> buildSymptomMap(JSONArray array) throws JSONException {
                HashMap<String,String> symptomMap = new HashMap<>();
                for(int i=0;i<array.length();i++){
                    JSONObject jsonObject = array.getJSONObject(i);
                    symptomMap.put(jsonObject.getString("name"),jsonObject.getString("id"));

                }
                return symptomMap;
            }


            @Override
            public void symptompsDetected(String s) {
                responseInfermedica.symptomPopup(s);
            }

            @Override
            public void finalDisease(JSONObject s) {
                try {
                    responseInfermedica.symptomPopup(s.getString("common_name"));
                    responseInfermedica.endSession(s.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    responseInfermedica.endSession(null);
                }
            }
        };

    }

    public void sendResponse(String id){
        sendResponse(id,true);
    }

    public void sendResponse(String id,boolean isPresent){
        String choice = "absent";
        if(isPresent)
            choice = "present";
        instance.proceed(id,choice);
    }

    public void sendResponse(List<String> ids) throws JSONException {
        for (String id : ids)
            instance.addSymptom(id,"present");
        instance.getNextQns();
    }


    public interface ResponseInfermedica{
        public void yesNoQns(String symptomId, String qn,HashMap<String,String> options);

        public void singleOptionQn(String mainText, HashMap<String,String> options);

        public void allowMultipleAns(String mainText, HashMap<String,String> options);

        public void endSession(String name);

        public void symptomPopup(String message);

    }


}
