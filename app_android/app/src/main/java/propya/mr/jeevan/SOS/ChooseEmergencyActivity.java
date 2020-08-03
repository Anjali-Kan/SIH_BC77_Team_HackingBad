package propya.mr.jeevan.SOS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Timer;

import butterknife.BindView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.UI_HELPERS.MainUIViewHolder;
import propya.mr.jeevan.R;
import propya.mr.jeevan.SOS.SOSActivitiy;


public class ChooseEmergencyActivity extends ActivityHelper {

    int[] drawables = {R.drawable.emergency_road_accident ,
            R.drawable.emergency_gunshot_stabbing,
            R.drawable.emergency_heart_problems, R.drawable.emergency_breathing_problems,
            R.drawable.emergency_fits_seizure ,  R.drawable.emergency_burns,
            R.drawable.emergency_maternity, R.drawable.emergency_other
    };
    @BindView(R.id.recyclerMainUI)
    RecyclerView mainUI;
    private String type_of_emergency = "unknown";

    String uid;

    static long lastActive = 0;

    final long maxCoolDownPeriod = 3000;

    boolean isEmergency = false;

    @Override
    public void userIdHelp(String uid) {
        super.userIdHelp(uid);
        stopProgress();
        if(uid==null){
//            showToast("couldn't deserialize");
            return;
        }
        this.uid = uid;
        showToast(uid);
    }

    @Override
    protected void viewReady(View v) {


        Date d = new Date();
        long time = d.getTime();

        if((time - lastActive) < maxCoolDownPeriod){
            finish();
            return;
        }
        lastActive = time;

        if(dataIntent!=null){
            isEmergency = dataIntent.getBooleanExtra("isEmergency",false);
            startProgress(null);
        }
        if(isEmergency){
            Intent intent = new Intent(ChooseEmergencyActivity.this, SOSActivitiy.class);
            intent.putExtra("type","User could not describe it");
            intent.putExtra("isEmergency",true);
            intent.putExtra("uidPatient", FirebaseAuth.getInstance().getCurrentUser().getUid());
            startActivity(intent);
            return;
        }
        setRecycler();
    }

    @Override
    protected int getRootView() {
        return R.layout.choose_emergency;
    }

    private void setRecycler() {
        mainUI.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL,false));
//        mainUI.setLayoutManager(new LinearLayoutManager(this));
        mainUI.setAdapter(new MainUIAdapter(this, v -> {
            switch (((int)v.getTag())){
                case 0:
                    ////todo road acc
                    type_of_emergency = "road_accident";
                    break;
                case 1:
                    //todo gunshot stabbing
                    type_of_emergency = "gunshot_or_stabbing";
                    break;
                case 2:
                    ////todo heart probs
                    type_of_emergency = "heart_problem";
                    break;
                case 3:
                    ////todo breathing probs
                    type_of_emergency = "breathing_problem";
                    break;
                case 4:
                    ////todo fits seizures
                    type_of_emergency = "fits_or_seizure";
                    break;
                case 5:
                    ////todo burns
                    type_of_emergency = "burns";
                    break;
                case 6:
                    ////todo maternity
                    type_of_emergency = "maternity_or_child";
                case 7:
                    ////todo other
                    type_of_emergency = "unknown";
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + ((int) v.getTag()));
            }
            Intent intent = new Intent(ChooseEmergencyActivity.this, SOSActivitiy.class);
            intent.putExtra("type",type_of_emergency);
            if(uid!=null)
                intent.putExtra("uidPatient",uid);
            startActivity(intent);
        },drawables));
    }


    public class MainUIAdapter extends RecyclerView.Adapter<MainUIViewHolder>{

        Context c;
        View.OnClickListener listener;
        int[] images;

        public MainUIAdapter(Context c,View.OnClickListener listener,int[] drawables) {
            this.c = c;
            this.listener = listener;
            this.images = drawables;
        }

        @NonNull
        @Override
        public MainUIViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainUIViewHolder(LayoutInflater.from(c).inflate(R.layout.view_holder_mainui,parent,false),listener);
        }

        @Override
        public void onBindViewHolder(@NonNull MainUIViewHolder holder, int position) {
            holder.imageView.setImageDrawable(c.getResources().getDrawable(images[position]));
            holder.imageView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }
    }



}
