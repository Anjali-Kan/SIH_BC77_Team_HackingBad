package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;

import propya.mr.jeevan.BluePrint.Hospital;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.R;
import propya.mr.jeevan.SchemeHelper;

public class HospitalDetails extends AppCompatActivity {

    Hospital hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);
        Intent intent = getIntent();
        hospital = intent.getParcelableExtra("Hospital");

        TextView name = findViewById(R.id.hospital_name);
        name.setText(hospital.hospitalName);

        TextView phone = findViewById(R.id.hospital_phone);
        phone.setText(hospital.phone);

        TextView address = findViewById(R.id.hospital_address);
        address.setText(hospital.hospitalAddress);

        ((TextView)findViewById(R.id.hospital_rating)).setText(String.format("Rating %s/5",hospital.rating));
//        TextView budget = findViewById(R.id.hospital_budget);
//        budget.setText(String.valueOf(hospital.budget));
    }

    public void checkDocs(View view) {
        startActivity(new Intent(this,DoctorSearch.class));
    }

    public void getDirections(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format(Constants.NearbyPlacesConstants.MAPS_INTENT_MARKER,hospital.hospitalLocation.getLatitude(),
                hospital.hospitalLocation.getLongitude())));
        startActivity(intent);

    }

    public void showDialog(View view) {
        String head = ((Chip)view).getText().toString();
        SchemeHelper helper = new SchemeHelper();
        helper.headName = head;
        helper.show(getSupportFragmentManager(),"sasasasa");


    }
}