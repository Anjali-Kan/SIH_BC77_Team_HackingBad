package propya.mr.jeevan.BluePrint;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public class Appointment {
    public String diagnosis,docId,hospitalId,patientId,prescription;
    public Date date;
    public String appointmentId;
    public int state;
//    0 pending 1 accepted -1 rejected
    public boolean isPaid,isAvail;

    public Appointment(){}

    public Appointment(@NonNull DocumentSnapshot documentSnapshot) throws NullPointerException {
        appointmentId = documentSnapshot.getId();
        date = new Date(documentSnapshot.getLong("timeLong"));
        diagnosis = documentSnapshot.getString("diagnosis");
        docId = documentSnapshot.getString("docId");
        hospitalId = documentSnapshot.getString("hospitalId");
        patientId = documentSnapshot.getString("patientId");
        prescription = documentSnapshot.getString("prescription");
        state = documentSnapshot.get("state",Integer.class);
        isPaid = documentSnapshot.getBoolean("isPaid");
        isAvail = false;
    }

    public Appointment(String docId,int startTime,Date d) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy:MM:dd");
        try {
            d = dateFormat.parse(dateFormat.format(d));
        } catch (ParseException e) {
            Log.d("Error parsing",e.getLocalizedMessage());
        }

        this.docId = docId;
        this.isAvail = true;
        this.isPaid = false;
        long finalOffset = startTime;
        finalOffset*=(60*1000);
        this.date = new Date(d.getTime()+finalOffset);
        this.state = 0;
    }

    private int getTimeSelfFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String[] split = dateFormat.format(date).split(":");
        return Integer.parseInt(split[0])*60+Integer.parseInt(split[1]);
    }

    public String[] getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("dd - MM - yy/h:mm a");
        return format.format(date).split("/");
    }

    private static List<Appointment> getArray(QuerySnapshot querySnapshot){
        ArrayList<Appointment> appointments = new ArrayList<>();
        appointments.clear();
        for (DocumentSnapshot document : querySnapshot.getDocuments())
            appointments.add(new Appointment(document));
        return appointments;
    }

    public void getEmptySlots(String docId,Date d,String slot,int slotSize,AppointmentCallBacks callBacks){
        String[] split = slot.split("/");
        String[] _start = split[1].split(":");
        String[] _end = split[2].split(":");
        int startTime = Integer.parseInt(_start[1])+Integer.parseInt(_start[0])*60;
        int endTime = Integer.parseInt(_end[1])+Integer.parseInt(_end[0])*60;
        if(endTime<startTime){
            endTime+=(24*60);
        }
        List<Appointment> available = new ArrayList<>();
        available.clear();
        int finalEndTime = endTime;
        getBookedSlots(docId,d, list->{
            HashSet<Integer> booked = new HashSet<>();
            booked.clear();
            available.clear();
            for (Appointment appointment : list) {
                booked.add(appointment.getTimeSelfFormat());
            }
            int currentTime = startTime;
            while (currentTime<finalEndTime){
                if(!booked.contains(currentTime))
                    available.add(new Appointment(docId,currentTime,d));
                currentTime+=slotSize;
            }
            callBacks.getAppointments(available);
        },false,false);
    }

    public void bookAppointment(Appointment appointment,@Nullable MakeAppointment callback) throws InvalidPropertiesFormatException {
        HashMap<String,Object> values = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy/HH:mm");
        String[] format = dateFormat.format(appointment.date).split("/");

        if(appointment.hospitalId== null && appointment.docId==null)
            throw new InvalidPropertiesFormatException("Both hospital and doc cannot be empty");

        values.put("date",format[0]);
        values.put("time",format[1]);
        values.put("diagnosis",appointment.diagnosis);
        values.put("docId",appointment.docId);
        values.put("hospitalId",appointment.hospitalId);
        values.put("isPaid",appointment.isPaid);
        values.put("patientId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        values.put("prescription",appointment.prescription);
        values.put("state",appointment.state);
        values.put("timeLong",appointment.date.getTime());


        FirebaseFirestore.getInstance().collection("appointments").add(values).addOnCompleteListener(t->{
            if(!t.isSuccessful()){
                Log.d("Error writing",t.getException().getLocalizedMessage());
                if(callback!=null)
                    callback.appointmentSuccess(null);
            }else
                if(callback!=null)
                    callback.appointmentSuccess(t.getResult().getId());
        });
    }

    public void getBookedSlots(String docId,Date date,@NonNull AppointmentCallBacks callBacks,boolean greaterThan,boolean isLive) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yy");
        String format = dateFormat.format(date);
        Query query = FirebaseFirestore.getInstance().collection("appointments");

        query.whereEqualTo("docId", docId);
        if(!greaterThan)
            query.whereEqualTo("date",format);
        else
            query.whereGreaterThanOrEqualTo("timeLong",date.getTime());

        if(isLive){
            query.addSnapshotListener((querySnapshot, e) -> {
                Log.d("Appointment data","data received");
                if(e!=null){
                    callBacks.getAppointments(null);
                    Log.d("Appointment data","error occurred "+e.getLocalizedMessage());
                    return;
                }
                callBacks.getAppointments(getArray(querySnapshot));
            });
        }else{
            query.get().addOnCompleteListener(t->{
                if(!t.isSuccessful()){
                    callBacks.getAppointments(null);
                    Log.d("Appointment data","error occurred "+t.getException().getLocalizedMessage());
                    return;
                }
                callBacks.getAppointments(getArray(t.getResult()));
            });
        }

    }

    public interface AppointmentCallBacks{
        public void getAppointments(List<Appointment> appointments);
    }

    public interface MakeAppointment{
        void appointmentSuccess(String appointmentId);
    }

}
