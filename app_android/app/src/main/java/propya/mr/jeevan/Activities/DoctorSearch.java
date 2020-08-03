package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;

import propya.mr.jeevan.Adapters.DoctorsAdapter;
import propya.mr.jeevan.BluePrint.Doctor;
import propya.mr.jeevan.Constants;
import propya.mr.jeevan.Helpers.AlgoliaQueryBuilder;
import propya.mr.jeevan.R;

public class DoctorSearch extends AppCompatActivity {
    static AlgoliaQueryBuilder builder;
    private EditText et_query;
    private Button btn_submit;
    String userInput = "";
    TextView tv_search_res;
    AlgoliaQueryBuilder dqb;
    Index index;
    ArrayList<Doctor> doctors = new ArrayList<>();
    DoctorsAdapter doctorsAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_doctor_search);

        Client client = new Client(Constants.AlgoliaConstants.APP_ID, Constants.AlgoliaConstants.API_KEY);
        index = client.initIndex(Constants.AlgoliaConstants.D_INDEX);

        et_query = findViewById(R.id.query_et_doctor);
        tv_search_res = findViewById(R.id.search_result_tv_doctor);
        fab = findViewById(R.id.floating_action_button);

        setQueryLogic();
        setLiveSearch();
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                userInput = et_query.getText().toString();
//                if(userInput.length() > 0) {
//                    builder.search(userInput);
////                    startActivity(new Intent(DoctorSearch.this, KnowTheDoctor.class)
////                            .putExtra("userInput", userInput));
//                }
//            }
//        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInput = et_query.getText().toString();
                startActivity(new Intent(DoctorSearch.this, KnowTheDoctor.class)
                        .putExtra("userInput", userInput));
            }
        });

        // Initialize doctors list
        index.searchAsync(new Query(), (jsonObject, e) -> {
            try {
//                hospitals.clear();
                Log.d("query was",""+jsonObject.getJSONArray("hits"));
                doctors.addAll(Doctor.parseJSON(jsonObject.getJSONArray("hits")));
                doctorsAdapter.notifyDataSetChanged();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        });

        doctorsAdapter = new DoctorsAdapter(this, doctors);

        ListView hospitalListView = findViewById(R.id.doctor_list);
        hospitalListView.setAdapter(doctorsAdapter);


//        hospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Hospital hospital = hospitals.get(i);
//                Intent intent = new Intent(HospitalSearch.this, HospitalDetails.class);
//                intent.putExtra("Hospital", hospital);
//                startActivity(intent);
//            }
//        });
    }

    void setLiveSearch(){
        et_query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if(s.length()<3){
                    builder.search(null);
                }else{
                    builder.search(s1);
                }
            }
        });


    }

    void setQueryLogic(){
        dqb = new AlgoliaQueryBuilder((userInput, filters)->{
            Query query = new Query();
            if(userInput!=null && !userInput.isEmpty())
                query = new Query(userInput);
            index.searchAsync(query.setFilters(filters), (jsonObject, e) -> {
                try {
                    Log.d("query was",""+jsonObject.getJSONArray("hits").toString());
                    tv_search_res.setText(jsonObject.getJSONArray("hits").toString());
                    doctors.clear();
                    doctors.addAll(Doctor.parseJSON(jsonObject.getJSONArray("hits")));
                    doctorsAdapter.notifyDataSetChanged();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            });

        });
        KnowTheDoctor.builder = dqb;
        builder = dqb;
    }
}

