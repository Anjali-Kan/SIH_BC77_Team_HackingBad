package propya.mr.jeevan.Helpers;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import propya.mr.jeevan.R;

public class InfermedicaOffline implements InfermedicaInstance.ResponseInfermedica{

    String instanceId;
    InfermedicaInstance instance;
    Context c;
    SendMessage sendMessageListener;
    int typePrevQn = -1;
    HashMap<String,String> prevSymptomMap = new HashMap<>();
    private String prevStringId = "";//used in case of yesNo qn
    private final String optionTextTemplate = "%s %s\n";

    public InfermedicaOffline(String instanceId, Context c,int age,String sex,SendMessage sendMessageListener) {
        this.instanceId = instanceId;
        this.c = c;
        this.instance = new InfermedicaInstance(c,sex.charAt(0),age,this);
        this.sendMessageListener = sendMessageListener;
    }

    public void responseReceived(String msg){
        Log.d("response received",msg);
        if (msg.trim().isEmpty()) {
            sendMessage(R.string.invalid_format_selection);
            return;
        }
        try {
            int response = Integer.parseInt(msg);
            handleResponse(response);
            return;
        }catch (Exception e){}
        try {
            List<Integer> responses = new ArrayList<>();
            for (String s : msg.split(",")) {
                responses.add(Integer.parseInt(s));
            }
            handleResponse(responses);
            return;
        }catch (Exception e){}
        if(typePrevQn!=-1)
            sendMessage(R.string.invalid_format_after_instance);
        else{
            instance.ask1stQn(msg);
        }

    }

    private void sendMessage(String msg){
        sendMessageListener.sendMessage(instanceId,msg);
    }
    private void sendMessage(int msg){
        sendMessage(c.getResources().getString(msg));
    }



    private void handleResponse(List<Integer> responses) {
        ArrayList<String> strings = new ArrayList<>(prevSymptomMap.keySet());
        ArrayList<String> symptomsIds = new ArrayList<>();
        for (Integer respons : responses) {
            try {
                symptomsIds.add(prevSymptomMap.get(strings.get(respons-1)));
            }catch (Exception e){
                sendMessage(R.string.invalid_format_selection);
                return;
            }
        }
        try {
            if (typePrevQn==0) {
                instance.sendResponse(prevStringId,symptomsIds.equals("present"));
            }else{
                instance.sendResponse(symptomsIds);
            }

        } catch (JSONException e) {
            sendMessage(R.string.invalid_format_selection);
        }


    }

    private void handleResponse(int response) {
        if(typePrevQn == 2){
            handleResponse(Collections.singletonList(response));
            return;
        }

        if(typePrevQn != 0 && typePrevQn!=1){
            sendMessage(R.string.invalid_format_selection);
            return;
        }
        ArrayList<String> strings = new ArrayList<>(prevSymptomMap.keySet());
        try {
            instance.sendResponse(prevSymptomMap.get(strings.get(response-1)));
        }catch (Exception e){
            sendMessage(R.string.invalid_format_selection);
        }
    }

    private void translateMsg(String text,HashMap<String,String> options){
        int i=1;
        StringBuilder optionText = new StringBuilder();
        for (String s : options.keySet())
            optionText.append(String.format(optionTextTemplate, i++, s));
        sendMessage(text);
        sendMessage(optionText.toString());
        this.prevSymptomMap = options;
    }


    @Override
    public void yesNoQns(String symptomId, String qn,HashMap<String, String> options) {
        this.typePrevQn = 0;
        this.prevStringId = symptomId;
        translateMsg(qn,options);
    }

    @Override
    public void singleOptionQn(String mainText, HashMap<String, String> options) {
        this.typePrevQn = 1;
        translateMsg(mainText,options);
    }

    @Override
    public void allowMultipleAns(String mainText, HashMap<String, String> options) {
        this.typePrevQn = 2;
        translateMsg(mainText,options);
    }

    @Override
    public void endSession(String disease) {
        this.symptomPopup(disease);
        sendMessage(R.string.bye_mf);
    }

    @Override
    public void symptomPopup(String message) {
        sendMessage("Probability of "+message);
    }

    public interface SendMessage{
        public void sendMessage(String instanceId,String msg);
        void endSession(String instanceId);
    }

}
