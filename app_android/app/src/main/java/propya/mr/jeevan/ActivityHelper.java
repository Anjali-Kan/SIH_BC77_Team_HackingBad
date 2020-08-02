package propya.mr.jeevan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;


public abstract class ActivityHelper extends AppCompatActivity {
    int viewId;
    View rootView;
    Toast t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewId = getRootView();
        rootView = LayoutInflater.from(this).inflate(viewId,null,false);
        setContentView(rootView);
        ButterKnife.bind(this);
        viewReady(rootView);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void showToast(String text){
        if(t!=null)
            t.cancel();
        t = Toast.makeText(this,text,Toast.LENGTH_LONG);
        t.show();
    }

    protected void log(String text){
        Log.v(this.getLocalClassName(),text);
    }


    protected abstract void viewReady(View v);
    protected abstract int getRootView();
}
