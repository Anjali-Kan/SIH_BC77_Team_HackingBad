package propya.mr.jeevan.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InvalidPropertiesFormatException;

import butterknife.BindView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.BluePrint.Appointment;
import propya.mr.jeevan.BottomFragHelper;
import propya.mr.jeevan.R;

public class UnNecessaryBottom extends BottomFragHelper {

    public String docId,availableSlots;
    public ActivityHelper activityHelper;

    @BindView(R.id.center_linear)
    LinearLayout layout;

    @Override
    protected int getLayoutId() {
        return R.layout.generic_centre_linear;
    }

    ArrayList<Appointment> appointmentsGlobal = new ArrayList<>();

    @Override
    protected void layoutReady(View view) {
        Appointment appointment = new Appointment();
        appointment.getEmptySlots(docId, new Date(), availableSlots, 10, appointments -> {
            this.appointmentsGlobal.clear();
            this.appointmentsGlobal.addAll(appointments);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd  MM  h:mm a  ");
            for (Appointment appointment1 : appointments) {
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.adapter_set_appointment, null, false);
                TextView textView = inflate.findViewById(R.id.writeOnMe);
                inflate.setTag(appointments.indexOf(appointment1));
                textView.setText(String.format("Slot : %s",dateFormat.format(appointment1.date)));
                inflate.setOnClickListener(v->{
                    int position = (int)inflate.getTag();
                    Appointment a = appointmentsGlobal.get(position);
                    a.docId = docId;
                    try {
                        new Appointment().bookAppointment(a,null);
                        activityHelper.showToast("Appointment done");
                    } catch (InvalidPropertiesFormatException e) {
                        e.printStackTrace();
                        activityHelper.showToast("Error adding appointment");
                    }
                    dismiss();
                });
                layout.addView(inflate);
            }
        });
    }
}
