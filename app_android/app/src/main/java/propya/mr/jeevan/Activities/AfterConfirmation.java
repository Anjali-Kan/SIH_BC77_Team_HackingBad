package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.ButterKnife;
import propya.mr.jeevan.Adapters.FirstAidAdapter;
import propya.mr.jeevan.R;

//TODO just get the info from some activity into bundle
//urls for exo player, exo player itself
public class AfterConfirmation extends AppCompatActivity {

    String [] first_aid_urls;//array in case more than 2
   String first_aid_title;
   String first_aid_text;
   private RecyclerView first_aid_recycler;
    private RecyclerView.Adapter first_aid_adapter;
    private RecyclerView.LayoutManager first_aid_layout_manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_confirmation);
        first_aid_recycler=findViewById(R.id.first_aid_recycler);
        Bundle bundle = getIntent().getExtras();
        first_aid_title=bundle.getString("first_aid_type");
        first_aid_text=bundle.getString("first_aid_text");
        first_aid_urls=bundle.getStringArray("first_aid_urls");
        first_aid_layout_manager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        first_aid_recycler.setLayoutManager(first_aid_layout_manager);
        Log.i("FIRSTAID",first_aid_urls.toString());
        first_aid_adapter= new FirstAidAdapter(first_aid_title,first_aid_text,first_aid_urls,AfterConfirmation.this);
        first_aid_recycler.setAdapter(first_aid_adapter);


    }
}
