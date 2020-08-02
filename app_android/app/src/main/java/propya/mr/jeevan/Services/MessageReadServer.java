package propya.mr.jeevan.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.util.HashMap;

import propya.mr.jeevan.Helpers.InfermedicaInstance;
import propya.mr.jeevan.Helpers.InfermedicaOffline;
import propya.mr.jeevan.R;

public class MessageReadServer extends Service {

    HashMap<String, InfermedicaOffline> instances = new HashMap<>();

    private InfermedicaOffline.SendMessage sendMessageCallBack = new InfermedicaOffline.SendMessage() {
        @Override
        public void sendMessage(String instanceId, String msg) {
            if (instances.get(instanceId)==null) {
                return;
            }
            sendSMS(instanceId,msg.trim());
        }

        @Override
        public void endSession(String instanceId) {
            if (instances.get(instanceId)==null) {
                return;
            }
            instances.remove(instanceId);
        }
    };


    public MessageReadServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FCMBase base = new FCMBase();
        base.createNotificationChannel(this);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Reading messages")
                .setContentText("Trying to read jeevan")
                .setPriority(NotificationCompat.PRIORITY_MIN);

        startForeground(769,builder.build());
        smsBroadcast();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    void smsBroadcast(){
        String broadCastString = "android.provider.Telephony.SMS_RECEIVED";

        IntentFilter filter = new IntentFilter(broadCastString);
        filter.setPriority(Integer.MAX_VALUE);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
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
                        if(isOurs(messages[0].getMessageBody()))
                            handleMessage(messages[0].getOriginatingAddress(),messages[0].getMessageBody());
                    }
                }
            }
        },filter);
    }

    private void handleMessage(String originatingAddress, String messageBody) {
        String[] s = messageBody.split(" ");

        if(s.length >= 2 && s[1].equals("NEW")){
            try {
                int age = Integer.parseInt(s[2]);
                String gender = s[3];
                if (!(gender.equals("F") || gender.equals("M"))){
                    throw new Exception("");
                }
                instances.put(originatingAddress,new InfermedicaOffline(originatingAddress,this,age,gender,sendMessageCallBack));
                sendMessageCallBack.sendMessage(originatingAddress,getResources().getString(R.string.register_success));
            }catch (Exception e){
                sendSMS(originatingAddress,getResources().getString(R.string.invalid_new));
            }
            return;
        }
        if(!instances.containsKey(originatingAddress) || instances.get(originatingAddress)==null){
            sendSMS(originatingAddress,getResources().getString(R.string.instance_not_found));
            return;
        }
        instances.get(originatingAddress).responseReceived(messageBody.replace("JEEVAN ",""));

    }

    boolean isOurs(String msg){
        try {
            return msg.split(" ")[0].equals("JEEVAN");
        }catch (Exception e){
            return false;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    void sendSMS(String phoneNo, String msg) {
        if(msg.length()>100){
            String[] s = msg.split(" ");
            int mid = s.length/2;
            StringBuilder builderLeft = new StringBuilder();
            for(int i=0;i<mid;i++)
                builderLeft.append(String.format("%s ",s[i]));
            StringBuilder builderRight = new StringBuilder();
            for(int i=mid;i<s.length;i++)
                builderRight.append(String.format("%s ",s[i]));
            sendSMS(phoneNo,builderLeft.toString());
            sendSMS(phoneNo,builderRight.toString());
            return;
        }


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
