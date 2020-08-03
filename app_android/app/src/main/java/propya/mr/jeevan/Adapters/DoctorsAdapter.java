package propya.mr.jeevan.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import propya.mr.jeevan.Activities.DoctorDetails;
import propya.mr.jeevan.BluePrint.Doctor;
import propya.mr.jeevan.R;

public class DoctorsAdapter extends ArrayAdapter<Doctor> {

    Context c;

    public DoctorsAdapter(@NonNull Context context, ArrayList<Doctor> doctors) {
        super(context, 0, doctors);
        this.c = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Doctor doctor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hospital_list_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.hospital_name);
        TextView phone = convertView.findViewById(R.id.hospital_phone);
        TextView address = convertView.findViewById(R.id.hospital_address);
        address.setVisibility(View.GONE);

        name.setText(doctor.name);
        phone.setText(String.format("%s       Exp: %s yrs",doctor.degree,doctor.experience));

        convertView.setOnClickListener(v->{
            Intent i = new Intent(c, DoctorDetails.class);
            i.putExtra("doctor",doctor);
            c.startActivity(i);
        });

//        address.setText(hospital.hospitalAddress);

        return convertView;
    }
}
