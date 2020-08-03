package propya.mr.jeevan.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.BluePrint.Appointment;
import propya.mr.jeevan.BluePrint.Doctor;
import propya.mr.jeevan.BottomFragHelper;
import propya.mr.jeevan.R;

public class DoctorDetails extends ActivityHelper {

    String docId;

    @BindView(R.id.docImage)
    CircleImageView docImage;

    @BindView(R.id.docName)
    TextView docName;

    @BindView(R.id.docSubText)
    TextView docSubText;

    @BindView(R.id.docSpecialisations)
    ChipGroup specializations;

    @BindView(R.id.docSlots)
    ChipGroup docSlots;

    String defaultImgUrl = "";

    String availableSlots;


    @Override
    protected void viewReady(View v) {

        Doctor doctor = dataIntent.getParcelableExtra("doctor");
        if(doctor==null){
            showToast("Some error occurred");
        }
        if (doctor == null)
            docId = "3P02p6xgOQZ2rWeMPrWy";
        else
            docId = doctor.selfUid;
//        showToast(doctor.selfUid);
        fetchDoctor(this.docId);
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_doctor_details;
    }


    void fetchDoctor(String docId){
        startProgress(null);

        FirebaseFirestore.getInstance().collection("doctors").document(docId).get().addOnCompleteListener(task -> {
            stopProgress();
            if(!task.isSuccessful()){
                showToast("Some error occurred");
                finish();
                return;
            }
            setViews(task.getResult());
        });




    }

    private void setViews(DocumentSnapshot result) {
        docName.setText(result.getString("name"));
        try {
            Glide.with(this).load(result.getString("image")).into(docImage);
            log(result.getString("image"));
        }catch (Exception __){
            __.printStackTrace();
            try {
                Glide.with(this).load(defaultImgUrl).into(docImage);
            }catch (Exception _){
                _.printStackTrace();
                docImage.setVisibility(View.GONE);
            }
        }
        docSubText.setText(String.format("Rating %s/5",result.getDouble("rating")));
        ArrayList<String> specialist = (ArrayList<String>) result.get("specialist");
        for(String s:specialist){
//            Chip c = new Chip(this);
//            c.setText(s);
//            c.setTextAppearanceResource(R.style.ForChips);
//            specializations.addView(c);
        }
        ArrayList<String> slots = (ArrayList<String>) result.get("slots");

        for(String s:slots){
            availableSlots = s;
//            Chip c = new Chip(this);
//            c.setText(s);
//            c.setTextAppearanceResource(R.style.ForChips);
//            docSlots.addView(c);
        }

    }

    public void bookAppointment(View view) {

        if(docId==null){
            showToast("Error occurred while booking");
            return;
        }
//        getSupportFragmentManager().beginTransaction().add()
        UnNecessaryBottom bottom = new UnNecessaryBottom();
        bottom.activityHelper = this;
        bottom.availableSlots=availableSlots;
        bottom.docId = docId;
        bottom.show(getSupportFragmentManager(),"jkjj");


    }


}