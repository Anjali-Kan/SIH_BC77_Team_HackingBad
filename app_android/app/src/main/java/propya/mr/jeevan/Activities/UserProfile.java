package propya.mr.jeevan.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import propya.mr.jeevan.BluePrint.Scheme;
import propya.mr.jeevan.R;

public class UserProfile extends AppCompatActivity {

    private String TAG = "USER PROFILE";
    //name
    private EditText nameEditText;
    private String mName;

    // gender
    private String mGender = "Other";
    private Spinner mGenderSpinner;
    //private int mGender = GENDER_OTHER;

    //dob
    final private Calendar myCalendar = Calendar.getInstance();
    private EditText dateEditText;
    private String mDob;

    // weight
    EditText weightEditText;
    private int mWeight;

    // height
    private EditText heightEditText;
    private int mHeight;

    // blood group
    private String mBloodGroup = "Other";
    private Spinner mBloodGroupSpinner;

    // organ donor
    boolean mOrganDonor = false;
    private CheckBox organDonorCheckBox;

    // medical data
    private HashMap<String, Boolean> medicalConditions = new HashMap<>();
    private EditText allergiesEditText;
    private String allergies;
    private EditText medsEditText;
    private String medications;
    private EditText extraNotesEditText;
    private String extraNotes;

    // prefer gov hospital
    private boolean preferGovHospital = false;

    // emergency contacts
    private Map<String, String> emergencyContacts = new HashMap<>();

    // family doctor
    private String mFamilyDoctorName;
    private String mFamilyDoctorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // gender and bloodgroup spinners
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);
        mBloodGroupSpinner = (Spinner) findViewById(R.id.spinner_bloodgroup);
        setupSpinner();

        // dob
        dateEditText = (EditText) findViewById(R.id.birthday);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                mDob = updateLabel();
            }
        };
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(UserProfile.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        // submit
        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"name: "+mName);
                Log.i(TAG,"dob: "+mDob);
                Log.i(TAG,"gender: "+mGender);
                Log.i(TAG,"weight "+mWeight);
                for(String key : emergencyContacts.keySet())
                    Log.i(TAG,"em contact 1"+key);
                uploadData();

                finish();
            }
        });
    }

    private void uploadData() {

        // name
        nameEditText = findViewById(R.id.name);
        mName = nameEditText.getText().toString().trim();

        // weight
        weightEditText = (EditText) findViewById(R.id.edit_weight);
        String w = weightEditText.getText().toString();
        if(w.length()==0)
            mWeight = 0;
        else
            mWeight = Integer.parseInt(w);

        // height
        heightEditText = (EditText) findViewById(R.id.edit_height);
        String h = heightEditText.getText().toString();
        if(h.length()==0)
            mHeight = 0;
        else
            mHeight = Integer.parseInt(h);

        // organ donor
        organDonorCheckBox = findViewById(R.id.organ_donor);
        if(organDonorCheckBox.isChecked())
            mOrganDonor = true;

        // medical conditions
        CheckBox alcoholCheckBox = findViewById(R.id.alcoholic);
        if(alcoholCheckBox.isChecked())
            medicalConditions.put("alcoholic",true);
        else
            medicalConditions.put("alcoholic",false);
        CheckBox diabetesCheckBox = findViewById(R.id.diabetes);
        if(diabetesCheckBox.isChecked())
            medicalConditions.put("diabetes",true);
        else
            medicalConditions.put("diabetes",false);
        CheckBox heartCheckBox = findViewById(R.id.heart_disease);
        if(heartCheckBox.isChecked())
            medicalConditions.put("heart_disease",true);
        else
            medicalConditions.put("heart_disease",false);
        CheckBox injuryCheckBox = findViewById(R.id.injury);
        if(injuryCheckBox.isChecked())
            medicalConditions.put("injury_history",true);
        else
            medicalConditions.put("injury_history",false);
        CheckBox pregnantCheckBox = findViewById(R.id.pregnant);
        if(pregnantCheckBox.isChecked())
            medicalConditions.put("pregnant",true);
        else
            medicalConditions.put("pregnant",false);
        CheckBox smokerCheckBox = findViewById(R.id.smoker);
        if(smokerCheckBox.isChecked())
            medicalConditions.put("smoker",true);
        else
            medicalConditions.put("smoker",false);

        allergiesEditText = (EditText)findViewById(R.id.allergies);
        allergies = allergiesEditText.getText().toString();
        ArrayList<String> allergyList = new ArrayList<>(Arrays.asList(allergies.split(",")));

        medsEditText = (EditText) findViewById(R.id.meds);
        medications = medsEditText.getText().toString();
        ArrayList<String> medicationsList = new ArrayList<>(Arrays.asList(medications.split(",")));

        extraNotesEditText = (EditText) findViewById(R.id.extra_notes);
        extraNotes = extraNotesEditText.getText().toString();

        // prefer gov hospital
        CheckBox preferGovCheckBox = findViewById(R.id.prefer_gov_hospital);
        if(preferGovCheckBox.isChecked())
            preferGovHospital = true;


        // emergency contacts
        emergencyContacts.put( ((EditText)findViewById(R.id.emcontact_1_name)).getText().toString(),
                ((EditText)findViewById(R.id.emcontact_1_phone)).getText().toString());
        emergencyContacts.put( ((EditText)findViewById(R.id.emcontact_2_name)).getText().toString(),
                ((EditText)findViewById(R.id.emcontact_2_phone)).getText().toString());
        emergencyContacts.put( ((EditText)findViewById(R.id.emcontact_3_name)).getText().toString(),
                ((EditText)findViewById(R.id.emcontact_3_phone)).getText().toString());

        // fam doctor
        mFamilyDoctorName = ((EditText)findViewById(R.id.emdoctor_name)).getText().toString();
        mFamilyDoctorPhone = ((EditText)findViewById(R.id.emdoctor_phone)).getText().toString();

        Map<String, Object> patient = new HashMap<>();
        patient.put("name", mName);
        patient.put("gender", mGender);
        patient.put("dob", mDob);
        patient.put("weight",mWeight);
        patient.put("height",mHeight);
        patient.put("blood_group",mBloodGroup);
        patient.put("organ_donor",mOrganDonor);
        patient.put("medical_conditions",medicalConditions);
        patient.put("allergies",allergyList);
        patient.put("prefer_gov_hospital",preferGovHospital);
        patient.put("medications",medicationsList);
        patient.put("emergency_contacts",emergencyContacts);
        HashMap<String, String> familyDoctor = new HashMap<String, String>();
        familyDoctor.put(mFamilyDoctorName, mFamilyDoctorPhone);
        patient.put("family_doctor",familyDoctor);
        patient.put("other_notes", extraNotes);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });

    }

    private void setupSpinner() {
        //gender spinner
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                mGender = selection;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = getString(R.string.gender_other);
            }
        });

        //bloodgroup spinner
        ArrayAdapter bloodGroupSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_blood_group, android.R.layout.simple_spinner_item);

        bloodGroupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mBloodGroupSpinner.setAdapter(bloodGroupSpinnerAdapter);

        mBloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection2 = (String) parent.getItemAtPosition(position);
                mBloodGroup = selection2;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBloodGroup = getString(R.string.bloodgroup_unknown);
            }
        });
    }

    private String updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        String date = sdf.format(myCalendar.getTime());
        dateEditText.setText(date);

        return date;
    }


}
