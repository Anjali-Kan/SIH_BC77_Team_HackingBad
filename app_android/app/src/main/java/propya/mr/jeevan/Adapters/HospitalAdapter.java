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

import java.util.ArrayList;

import propya.mr.jeevan.Activities.HospitalDetails;
import propya.mr.jeevan.BluePrint.Hospital;
import propya.mr.jeevan.R;

public class HospitalAdapter extends ArrayAdapter<Hospital> {

    private static String[] offers = new String[]{"sevenhills","masrani"};
    Context c;

    public HospitalAdapter(@NonNull Context context, ArrayList<Hospital> hospitals) {
        super(context, 0, hospitals);
        this.c = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Hospital hospital = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hospital_list_item, parent, false);
        }
        convertView.findViewById(R.id.scamChip).setVisibility(View.GONE);
        for (String s:offers)
            if(hospital.hospitalName.toLowerCase().contains(s))
                convertView.findViewById(R.id.scamChip).setVisibility(View.VISIBLE);

        TextView name = convertView.findViewById(R.id.hospital_name);
        TextView phone = convertView.findViewById(R.id.hospital_phone);
        TextView address = convertView.findViewById(R.id.hospital_address);

        name.setText(hospital.hospitalName);
        phone.setText(hospital.phone);
        address.setText(hospital.hospitalAddress);

        convertView.setOnClickListener(v->{
            Intent intent = new Intent(c, HospitalDetails.class);
                    intent.putExtra("Hospital", hospital);
                c.startActivity(intent);
        });

        return convertView;
    }
}
