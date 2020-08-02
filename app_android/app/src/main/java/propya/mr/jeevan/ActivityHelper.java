package propya.mr.jeevan;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;

import butterknife.ButterKnife;

public abstract class ActivityHelper extends AppCompatActivity {
    int viewId;
    View rootView;
    Toast t;
    int requestIDs = 2000;
    protected Bundle extras;
    protected Intent dataIntent;
    HashMap<Integer,ActivityResultResponse> resultResponseHashMap = new HashMap<>();
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewId = getRootView();
        rootView = LayoutInflater.from(this).inflate(viewId,null,false);
        setContentView(rootView);
        ButterKnife.bind(this);
        try {
            dataIntent = getIntent();
            extras = getIntent().getBundleExtra("bundle");
        }catch (Exception e){
            Log.i("Bundle","No bundle attached");
            e.printStackTrace();
        }
        viewReady(rootView);
    }

    protected void startProgress(@Nullable String[] msgs){
        if(msgs==null)
            msgs = getResources().getStringArray(R.array.generic_progress);
        if(progressBar == null)
            progressBar= new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.setMessage(msgs[1]);
        progressBar.setTitle(msgs[0]);
        progressBar.show();
    }

    protected void stopProgress(){
        if(progressBar==null)
            return;
        progressBar.cancel();
        progressBar = null;
    }


    public void startActivity(Class c){
        startActivity(new Intent(this,c));
    }

    public void startActivity(Class c,Bundle b){
        Intent intent = new Intent(this, c);
        intent.putExtra("bundle",b);
        startActivity(intent);
    }
    public void startActivity(Uri u){
        Intent intent = new Intent(Intent.ACTION_VIEW,u);
        startActivity(intent);
    }

    public void startActivity(String s){
        startActivity(Uri.parse(s));
    }

    public void exit(){
        finishAndRemoveTask();
        System.exit(0);
    }

    public void showToast(String text){
        if(t!=null)
            t.cancel();
        t = Toast.makeText(this,text,Toast.LENGTH_LONG);
        t.show();
    }

    protected void log(String text){
        Log.v(this.getLocalClassName(),text);
    }

    public void startActivityForResult(Intent intent,ActivityResultResponse callBack) {
        int requestCode = requestIDs++;
        resultResponseHashMap.put(requestCode,callBack);
        super.startActivityForResult(intent, requestCode);
    }

    public void showAlert(String[] msgs, @Nullable String buttonText, AlertDialogListener listener){
        if(buttonText==null)
            buttonText = "Ok";
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(msgs[0]).setMessage(msgs[1]).setPositiveButton(buttonText,
                (dialog, which) ->  listener.closedDialog(true,dialog)

        );
        builder.setOnCancelListener(dialog -> listener.closedDialog(false,dialog));
        builder.show();

    }
    public void showAlert(String[] msgs, @Nullable String buttonText,String negative, AlertDialogListener listener){
        if(buttonText==null)
            buttonText = "Ok";
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(msgs[0]).setMessage(msgs[1]).setPositiveButton(buttonText,
                (dialog, which) ->  listener.closedDialog(true,dialog)

        ).setNegativeButton(negative,((dialog, which) -> listener.closedDialog(false,dialog)));
        builder.setOnCancelListener(dialog -> listener.closedDialog(false,dialog));
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultResponseHashMap.containsKey(requestCode))
            if(resultResponseHashMap.get(requestCode)!=null)
                resultResponseHashMap.get(requestCode).result(data,resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public interface AlertDialogListener{
        public void closedDialog(boolean didAccept, DialogInterface dialog);
    }

    public interface ActivityResultResponse{
        public void result(Intent data,int result);
    }

    protected abstract void viewReady(View v);
    protected abstract int getRootView();
}
