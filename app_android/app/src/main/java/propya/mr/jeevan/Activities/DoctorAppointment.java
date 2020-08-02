package propya.mr.jeevan.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import propya.mr.jeevan.ActivityHelper;
import propya.mr.jeevan.BluePrint.Appointment;
import propya.mr.jeevan.R;

public class DoctorAppointment extends ActivityHelper {

    @BindView(R.id.docAppointmentsRecycler)
    RecyclerView recyclerView;
    AdapterSelf adapter;

    @BindView(R.id.docAppointAccSwitch)
    Switch acceptSwitch;

    @BindView(R.id.docAppointRejSwitch)
    Switch rejSwitch;

    @BindView(R.id.docAppointRevSwitch)
    Switch revSwitch;

    private boolean showAccepted=true,showRejected=true,reverseOrder,hasData;

    List<Appointment> listServer;


    @Override
    protected void viewReady(View v) {


        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            switch (buttonView.getId()){
                case R.id.docAppointRejSwitch:
                    showRejected = isChecked;
                    break;
                case R.id.docAppointAccSwitch:
                    showAccepted = isChecked;
                    break;
                case R.id.docAppointRevSwitch:
                    reverseOrder = isChecked;
                    break;
            }
            if(hasData)
                setAdapter(listServer);

        };

        revSwitch.setChecked(reverseOrder);
        rejSwitch.setChecked(showRejected);
        acceptSwitch.setChecked(showAccepted);

        revSwitch.setOnCheckedChangeListener(listener);
        acceptSwitch.setOnCheckedChangeListener(listener);
        rejSwitch.setOnCheckedChangeListener(listener);


        try {
            startProgress(null);
            getAppointments();
        } catch (ParseException e) {
            e.printStackTrace();
            stopProgress();
            showToast("Error getting date");
        }
    }

    @Override
    protected int getRootView() {
        return R.layout.activity_doctor_appointment;
    }


    public void showProgress(){
        startProgress(new String[]{"Just a second","We are updating your appointment please be patient"});
    }

    void getAppointments() throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yy:MM:dd");
        new Appointment().getBookedSlots(FirebaseAuth.getInstance().getUid(),format.parse(format.format(date)), l ->{
            hasData = true;
            this.listServer = l;
            setAdapter(l);
        },true,true);
    }

    private void setAdapter(List<Appointment> list) {

        if(list==null){
            showToast("Error occurred while getting appointments");
            stopProgress();
            return;
        }

        List<Appointment> newList = new ArrayList<>();

        for (Appointment appointment : list) {
            switch (appointment.state){
                case 0:
                   newList.add(appointment);
                   break;
                case -1:
                    if(showRejected)
                        newList.add(appointment);
                    break;
                case 1:
                    if(showAccepted)
                        newList.add(appointment);
                    break;
            }
        }

        list = newList;
        
        list.sort((o1, o2) -> {
            int multi = 1;
            if(reverseOrder)
                multi=-1;
            return (int)(o1.date.getTime()-o2.date.getTime())*multi;
        });

        stopProgress();



        if(recyclerView==null)
            return;

        adapter = new AdapterSelf(this,list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//
//        if(adapter == null){
//            }else{
//            adapter.newData(list);
//        }


    }

    private class AdapterSelf extends Adapter<ViewHolder> {
        Context c;
        List<Appointment> appointments;
        View.OnClickListener accept,reject;

        public AdapterSelf(Context c, List<Appointment> appointments) {
            this.c = c;
            this.appointments = appointments;
            this.accept = v->{
                  updateStatus((int)v.getTag(),1);
            };
            this.reject = v->{
                  updateStatus((int)v.getTag(),-1);
            };
        }

        private void updateStatus(int position,int status){
            HashMap<String,Object> updates = new HashMap<>();
            updates.put("state",status);
            String appointmentId = appointments.get(position).appointmentId;
            ((DoctorAppointment)c).showProgress();
            FirebaseFirestore.getInstance().collection("appointments").document(appointmentId).update(updates);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(c).inflate(R.layout.view_holder_doctor_appointment,parent,false));
        }

        public void newData(List<Appointment> appointments){
            this.appointments=appointments;
            notifyDataSetChanged();
        }



        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Appointment appointment = appointments.get(position);

            String[] dateTime = appointment.getDateTime();
            holder.time.setText(String.format("Time : %s",dateTime[1]));
            holder.date.setText(String.format("Date : %s",dateTime[0]));

            if(appointment.diagnosis!=null)
                holder.diagnosis.setText(appointment.diagnosis);

            switch (appointment.state){
                case 1://accept
                    holder.accept.setBackground(c.getDrawable(R.drawable.button_disabled));
                    holder.accept.setText("Accepted");
                    holder.accept.setEnabled(false);
                    holder.reject.setVisibility(View.GONE);
                    holder.diagnosis.setTextColor(Color.GREEN);
                    break;
                case 0:// didnt ack
                    holder.accept.setTag(position);
                    holder.reject.setTag(position);
                    holder.accept.setOnClickListener(accept);
                    holder.reject.setOnClickListener(reject);
                    break;
                case -1://rejected
                    holder.accept.setVisibility(View.GONE);
                    holder.reject.setBackground(c.getDrawable(R.drawable.button_disabled));
                    holder.reject.setEnabled(false);
                    holder.reject.setText("Rejected");
                    holder.diagnosis.setText("Declined");
                    holder.diagnosis.setTextColor(Color.RED);
                    break;
            }

//            if(appointment.state ==0){
//                holder.accept.setBackground(c.getDrawable(R.drawable.button_disabled));
//                holder.accept.setEnabled(false);
//                holder.diagnosis.setText("Declined");
//                holder.diagnosis.setTextColor(Color.RED);
//            }else{
//
//            }



        }

        @Override
        public int getItemCount() {
            return appointments.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        Button accept,reject;
        TextView time,date,diagnosis;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            accept = itemView.findViewById(R.id.docAppointAccept);
            reject = itemView.findViewById(R.id.docAppointReject);
            time = itemView.findViewById(R.id.docAppointTime);
            date = itemView.findViewById(R.id.docAppointDate);
            diagnosis = itemView.findViewById(R.id.docAppointDiagnosis);
        }
    }


}