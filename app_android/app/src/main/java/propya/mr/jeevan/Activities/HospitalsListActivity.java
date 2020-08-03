package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import propya.mr.jeevan.Adapters.HospitalAdapter;
import propya.mr.jeevan.BluePrint.Hospital;
import propya.mr.jeevan.R;

/* ___________________________________________________________________
                             PLEASE NOTE
                THIS ACTIVITY IS NOT IN USE
_______________________________________________________________
*/

public class HospitalsListActivity extends AppCompatActivity {

    public ArrayList<Hospital> hospitals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*THIS ACTIVITY IS NOT IN USE */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hospital_list_activity);

        /* CONSTRUCTOR of HOSPITAL class
        Hospital(String name, Location location, int budget, float rating, boolean burnWard, boolean emergency, boolean government,
                    boolean heartWard, String phone, String email, ArrayList<String> facilities, ArrayList<String> insurance,
                    ArrayList<String> schemes, ArrayList<String> specialists )
         */

        /*harcoded arraylist for testing
        hospitals = new ArrayList<Hospital>();
        ArrayList<String> facilities1 = new ArrayList<String>();
        facilities1.add("burns");
        facilities1.add("pre natal");
        ArrayList<String> facilities2 = new ArrayList<String>();
        facilities1.add("pediatrics");
        facilities1.add("pre natal");
        ArrayList<String> insurances = new ArrayList<>();
        insurances.add("HDFC");
        insurances.add("LIC");
        ArrayList<String> schemes = new ArrayList<>();
        schemes.add("XYZ");
        ArrayList<String> specialists = new ArrayList<>();
        insurances.add("xyz");
        insurances.add("doc");
        Location targetLocation = new Location("");//provider name is unnecessary
        targetLocation.setLatitude(0.0d);//your coords of course
        targetLocation.setLongitude(0.0d);

        hospitals.add(new Hospital("xyz", targetLocation, 4, 6.2f, true, true, false, false,
                "996788755", "xyz@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("abc", targetLocation, 5, 1.2f, true, true, false, false,
                "0000000000", "abc@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("kokilabem", targetLocation, 4, 6.2f, true, true, false, false,
                "996788755", "xyz@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("shatabdi", targetLocation, 5, 1.2f, true, true, false, false,
                "0000000000", "abc@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("xyz", targetLocation, 4, 6.2f, true, true, false, false,
                "996788755", "xyz@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("abc", targetLocation, 5, 1.2f, true, true, false, false,
                "0000000000", "abc@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("kokilabem", targetLocation, 4, 6.2f, true, true, false, false,
                "996788755", "xyz@gmail.com", facilities1, insurances, schemes, specialists));
        hospitals.add(new Hospital("shatabdi", targetLocation, 5, 1.2f, true, true, false, false,
                "0000000000", "abc@gmail.com", facilities1, insurances, schemes, specialists));
*/
        hospitals = this.getIntent().getExtras().getParcelableArrayList("hospitals");
        if(hospitals==null) {
            Log.e("Hospitals list", "couldnt pass araaylist");
        }
        HospitalAdapter hospitalAdapter = new HospitalAdapter(this, hospitals);

        ListView hospitalListView = findViewById(R.id.hospital_list);
        hospitalListView.setAdapter(hospitalAdapter);


        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Hospital hospital = hospitals.get(i);
                Intent intent = new Intent(HospitalsListActivity.this, HospitalDetails.class);
                intent.putExtra("Hospital", hospital);
                startActivity(intent);
            }
        });
    }



}