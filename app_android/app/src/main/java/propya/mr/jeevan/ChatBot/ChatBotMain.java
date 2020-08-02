package propya.mr.jeevan.ChatBot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import propya.mr.jeevan.Helpers.InferMedica;
import propya.mr.jeevan.R;

public class ChatBotMain extends AppCompatActivity {

    LinearLayout recyclerView;
    InferMedica inferMedica;
    String prevChat="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot_main);
        recyclerView = findViewById(R.id.recyclerChat);
        inferMedica = new InferMedica(this, new InferMedica.RepliesBot() {
            @Override
            public void replyFromBot(JSONObject s) {
                try {
                    ChatAdapter adapter = new ChatAdapter(ChatBotMain.this,inferMedica);
                    String text = s.getString("text");
                    if(text.equals(prevChat)){
                        this.symptompsDetected(inferMedica.lastResponse.getJSONArray("conditions").getJSONObject(0).getString("name"));
                        return;
                    }
                    prevChat = text;
                    adapter.received.setText(text);
                    adapter.received.setVisibility(View.VISIBLE);
                    String type = s.getString("type");
                    if(type.equals("single")){
                        adapter.getYesNo(s);

                    }else if(type.equals("group_single")){
                        adapter.getRadio(s);
                    }else {
                        adapter.getCheckBoxes(s);
                    }
                    recyclerView.addView(adapter.rootView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void symptompsDetected(String s) {
                ChatAdapter adapter = new ChatAdapter(ChatBotMain.this,inferMedica);
                adapter.symptom.setText(s);
                adapter.symptom.setVisibility(View.VISIBLE);
                recyclerView.addView(adapter.rootView);

            }

            @Override
            public void finalDisease(JSONObject s) {

            }
        },"male",30);


    }

    public void startChat(View view) {
        EditText viewById = (EditText) findViewById(R.id.chatEditText);
        inferMedica.sendMessage(viewById.getText().toString());
    }
}
