package propya.mr.jeevan.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.firebase.ui.auth.ui.ProgressView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import butterknife.BindView;
import propya.mr.jeevan.BottomFragHelper;
import propya.mr.jeevan.R;

public class DiagoniseBottomFrag extends BottomFragHelper {
    @BindView(R.id.prescription)
    EditText prescription;
    @BindView(R.id.diagonsis)
    EditText diagnosis;
    @BindView(R.id.complete_app)
    Button markComplete;
    @BindView(R.id.app_date)
    TextView appDate;
    @BindView(R.id.app_time)
    TextView appTime;
    @BindView(R.id.isPaid)
    CheckBox isPaid;
    String appointmentId;
    String date;
    String time;
    String apt_diagnosis, apt_prescription;

    public DiagoniseBottomFrag(String appointmentId,String date, String time, String apt_diagnosis, String apt_prescription)
    {
        this.appointmentId=appointmentId;
        this.date=date;
        this.time=time;
        this.apt_diagnosis= apt_diagnosis == null ? "" : apt_diagnosis;
        this.apt_prescription= apt_prescription == null ? "" : apt_prescription;

    }
    @Override
    protected int getLayoutId() {
        return R.layout.bot_frag_diagonise;    }

    @Override
    protected void layoutReady(View view) {//do stuff

                appDate.setText(date);
                appTime.setText(time);
                diagnosis.setText(apt_diagnosis);
                prescription.setText(apt_prescription);
                Log.d("pres and diag", apt_diagnosis+" and "+apt_prescription);
        markComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pres=prescription.getText().toString();
                String diag=diagnosis.getText().toString();
                boolean paid= isPaid.isChecked();

                //TODO dismiss the
                ProgressDialog prog;
                prog = new ProgressDialog(getContext());
                prog.show();
                HashMap<String,Object> updates = new HashMap<>();
                updates.put("state",10);
                updates.put("prescription",pres);
                updates.put("diagnosis",diag);
                updates.put("isPaid",paid);
                FirebaseFirestore.getInstance().collection("appointments").document(appointmentId).update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        prog.dismiss();
                        dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        prog.dismiss();
                        Log.e("Appointment complete","Updates on completing appointment failed");
                        dismiss();
                    }
                });


            }
        });
    }
}
