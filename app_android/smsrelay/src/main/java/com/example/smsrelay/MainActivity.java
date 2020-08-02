package com.example.smsrelay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsBroadcast();
    }


    void smsBroadcast(){
        String broadCastString = "android.provider.Telephony.SMS_RECEIVED";

        IntentFilter filter = new IntentFilter(broadCastString);
        filter.setPriority(Integer.MAX_VALUE);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Got message", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "Intent recieved: " + intent.getAction());


                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[])bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    }
                    if (messages.length > -1) {
                        Log.i("TAG", "Message recieved: " + messages[0].getMessageBody());
                        Log.i("TAG", "Message recieved: " + messages[0].getOriginatingAddress());
                        sendSMS(messages[0].getOriginatingAddress(), messages[0].getMessageBody().replaceAll("Chinu","Shanu"));
                    }
                }

            }
        },filter);



    }
    void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
