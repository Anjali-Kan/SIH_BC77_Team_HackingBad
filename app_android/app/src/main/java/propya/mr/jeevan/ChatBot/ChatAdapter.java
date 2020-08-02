package propya.mr.jeevan.ChatBot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import propya.mr.jeevan.Helpers.InferMedica;
import propya.mr.jeevan.R;

public class ChatAdapter {

    Context c;
    View rootView;

    RadioGroup radioGroupSelections;
    RadioGroup yesNo;
    LinearLayout checkBoxes;
    InferMedica inferMedica;

    public TextView symptom,sent,received;


    public ChatAdapter(Context c, InferMedica inferMedica) {
        this.c = c;
        this.inferMedica = inferMedica;
        rootView = LayoutInflater.from(c).inflate(R.layout.view_holder_chat,null,false);
        radioGroupSelections = rootView.findViewById(R.id.chatRadioGroup);
        yesNo = radioGroupSelections;
        checkBoxes = rootView.findViewById(R.id.chatLinear);
        received = rootView.findViewById(R.id.chatReceived);
        sent = rootView.findViewById(R.id.chatSent);
        symptom = rootView.findViewById(R.id.chatSymptom);
    }

    public View getYesNo(JSONObject jsonObject){
        radioGroupSelections.setVisibility(View.VISIBLE);
        try {
            final String string = jsonObject.getJSONArray("items").getJSONObject(0).getString("name");

            RadioButton radioButtonYes = new RadioButton(c);
            RadioButton radioButtonNo = new RadioButton(c);
            radioButtonYes.setText("Yes");
            radioButtonNo.setText("No");
            radioButtonNo.setOnClickListener(v -> {
                inferMedica.proceed(string,"absent");
                radioGroupSelections.setVisibility(View.GONE);
            });
            radioButtonYes.setOnClickListener(v -> {
                inferMedica.proceed(string,"present");
                radioGroupSelections.setVisibility(View.GONE);
            });
            radioGroupSelections.addView(radioButtonYes);
            radioGroupSelections.addView(radioButtonNo);

        }catch (Exception e){}
        return rootView;
    }

    public View getRadio(JSONObject jsonObject){
        radioGroupSelections.setVisibility(View.VISIBLE);
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0;i<items.length();i++){
                RadioButton radioButton = new RadioButton(c);
                radioButton.setText(items.getJSONObject(i).getString("name"));
                radioButton.setTag(items.getJSONObject(i).getString("id"));
                radioButton.setOnClickListener(v -> {
                    radioGroupSelections.setVisibility(View.GONE);
                    inferMedica.proceed(((String)v.getTag()),"present");
                });
                radioGroupSelections.addView(radioButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    public View getCheckBoxes(JSONObject jsonObject){
        radioGroupSelections.setVisibility(View.VISIBLE);
        try {
            JSONArray items = jsonObject.getJSONArray("items");
            for(int i=0;i<items.length();i++){
                RadioButton radioButton = new RadioButton(c);
                radioButton.setText(items.getJSONObject(i).getString("name"));
                radioButton.setTag(items.getJSONObject(i).getString("id"));
                radioButton.setOnClickListener(v -> {
                    radioGroupSelections.setVisibility(View.GONE);
                    inferMedica.proceed(((String)v.getTag()),"present");
                });
                radioGroupSelections.addView(radioButton);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }




}
