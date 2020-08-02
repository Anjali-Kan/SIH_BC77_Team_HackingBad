package propya.mr.jeevan.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.Date;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import butterknife.BindView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.BluePrint.Appointment;
import propya.mr.jeevan.R;

public class PatientAppointment extends ActivityHelper {

    @BindView(R.id.patientRecycler)
    RecyclerView recyclerView;

    @Override
    protected void viewReady(View v) {
        String doctorId = "";//extras.getString("doctorId");
        int slotSize = 30;
        String slots[] = new String[]{"monday/1:00/18:00"};
        doctorId = "NFyWflFjH7f6WrB51yhK";
        new Appointment().getEmptySlots(doctorId, new Date(),slots[0],slotSize,list->{
            setAdapter(list);
            showToast(" size of data"+list.size() );
            showAlert(new String[]{"Book appointment","1st will be booked from a total of "+list.size()},null,(accept,dialog)->{
                if(accept) {
                    try {
                        new Appointment().bookAppointment(list.get(0),id->{
                            showToast(id);
                            finish();
                        });
                    } catch (InvalidPropertiesFormatException e) {
                        showToast("error "+e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    void setAdapter(List<Appointment> list){


    }

    @Override
    protected int getRootView() {
        return R.layout.activity_patient_appointment;
    }





}