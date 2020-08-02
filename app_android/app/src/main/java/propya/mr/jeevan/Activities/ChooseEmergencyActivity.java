package propya.mr.jeevan.Activities;

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

import propya.mr.jeevan.UI_HELPERS.MainUIViewHolder;
import propya.mr.jeevan.R;
import propya.mr.jeevan.SOS.SOSActivitiy;


public class ChooseEmergencyActivity extends AppCompatActivity {

    int[] drawables = {R.drawable.emergency_road_accident ,
            R.drawable.emergency_gunshot_stabbing,
            R.drawable.emergency_heart_problems, R.drawable.emergency_breathing_problems,
            R.drawable.emergency_fits_seizure ,  R.drawable.emergency_burns,
            R.drawable.emergency_maternity, R.drawable.emergency_other
    };
    RecyclerView mainUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_emergency);
        mainUI = findViewById(R.id.recyclerMainUI);
        setRecycler();

    }

    private void setRecycler() {
        mainUI.setLayoutManager(new GridLayoutManager(this,2, RecyclerView.VERTICAL,false));
//        mainUI.setLayoutManager(new LinearLayoutManager(this));
        mainUI.setAdapter(new MainUIAdapter(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (((int)v.getTag())){
                    case 0:
                        ////todo road acc
                        break;
                    case 1:
                        //todo gunshot stabbing
                        break;
                    case 2:
                        ////todo heart probs
                        break;
                    case 3:
                        ////todo breathing probs
                        break;
                    case 4:
                        ////todo fits seizures
                        break;
                    case 5:
                        ////todo burns
                        break;
                    case 6:
                        ////todo maternity
                    case 7:
                        ////todo other
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + ((int) v.getTag()));
                }
                startActivity(new Intent(ChooseEmergencyActivity.this, SOSActivitiy.class));
            }
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
