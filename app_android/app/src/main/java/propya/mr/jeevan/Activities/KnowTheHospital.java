//package propya.mr.jeevan.Activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import java.util.List;
//
//import propya.mr.jeevan.R;
//
//public class KnowTheHospital extends AppCompatActivity {
//
//    int[] drawables ={
//            R.drawable.filter_location_card,
//            R.drawable.filter_rating_card, R.drawable.filter_budget_card,
//            R.drawable.filter_services_card , R.drawable.filter_specialist_card,
//            R.drawable.filter_insurance_card, R.drawable.filter_govschemes_card,
//
//
//    };
//    RecyclerView filterRecycler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_know_the_hospital);
//        filterRecycler=findViewById(R.id.filter_recyler);
//        setRecycler();
//    }
//
//    private void setRecycler()
//    {
//        filterRecycler.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
//        filterRecycler.setAdapter(new AdapterFilter(this,new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//
//                switch (((int)v.getTag())){
//                    case 0:
//                    //loc
//                        //TODO
//                        break;
//                    case 1:
//
//                        //rate
//                        break;
//                    case 2:
//                        //bud
//                        break;
//                    case 3:
//                        //serv
//                        break;
//                    case 4:
//                        //fac
//                        break;
//                    case 5:
//                        //spe
//                        break;
//                    case 6:
//                        //ins
//                        break;
//                    case 7:
//
//                        //govschemes
//                        break;
//
//                    default:
//                        throw new IllegalStateException("Unexpected value: " + ((int) v.getTag()));
//                }
//            }
//        }));
//    }
//}
//public class AdapterFilter extends RecyclerView.Adapter<FilterViewHolder>{
//
//    Context c;
//    View.OnClickListener listener;
//    int[] images;
//
//    public AdapterFilter(Context c,View.OnClickListener listener,int[] drawables) {
//        this.c = c;
//        this.listener = listener;
//        this.images = drawables;
//    }
//
//    @NonNull
//    @Override
//    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new FilterViewHolder(LayoutInflater.from(c).inflate(R.layout.adapter_filters,parent,false),listener);
//
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
//        (c.getResources().getDrawable(images[position]));
//        holder.se.setTag(position);
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return images.length;
//    }
//}
package propya.mr.jeevan.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import propya.mr.jeevan.Helpers.AlgoliaQueryBuilder;
import propya.mr.jeevan.R;

public class KnowTheHospital extends AppCompatActivity {

    ImageView applyFilters;

    String rating = "";
    String budget = "";
    ArrayList<String> services = new ArrayList<>();
    ArrayList<String> specialist = new ArrayList<>();
    ArrayList<String> insurance = new ArrayList<>();
    ArrayList<String> govScheme = new ArrayList<>();

    Map<String, String> schemeMap = new HashMap<>();



    static AlgoliaQueryBuilder builder;

    public int[] drawables ={
            R.drawable.filter_location_card,
            R.drawable.filter_rating_card, R.drawable.filter_budget_card,
            R.drawable.filter_services_card , R.drawable.filter_specialist_card,
            R.drawable.filter_insurance_card, R.drawable.filter_govschemes_card,
    };

    HashMap<String,String> replaceBy = new HashMap<>();

    int[] linearLayouts = {R.id.servicesLayout,R.id.specialistLayout,R.id.insuranceLayout,R.id.govSchemeLayout};
    String[][] data = {
            {"X ray","CT Scan","Dialysis"},
            {"Eye","Skin"},
            {"LIC", "Max Bupa", "Apollo", "HDFC"},
            {"UHIS", "CGHS", "AABY", "JBY"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_the_hospital);
        setRecycler();
        applyFilters = findViewById(R.id.applyFilters);
        applyFilters.setOnClickListener(v->setFilters());

        schemeMap.put("UHIS", "Universal Health Insurance Scheme");
        schemeMap.put("CGHS", "Central Government Health Scheme");
        schemeMap.put("AABY", "Aam Aadmi Bima Yojana");
        schemeMap.put("JBY", "Janashree Bima Yojana");

    }

    private void setFilters() {
        getSelectedData();

        builder.addRadioQuery("rating", rating);
        builder.addRadioQuery("budget", budget);

        Log.d("data fetched",services.toString());
        for(String s:services)
            builder.addAndQuery("facility",getMapping(s));

        if(specialist.size() > 0)
            builder.addOrQuery("specialists",specialist);

        if(insurance.size() > 0)
            builder.addOrQuery("insurance",insurance);

        if(govScheme.size() > 0)
            builder.addOrQuery("scheme",govScheme);

        finish();

        String searchString = getIntent().getExtras().getString("userInput");
        if(searchString.length()>0)
            builder.search(searchString);
    }

    private String getMapping(String s){
        if(replaceBy.containsKey(s))
            return replaceBy.get(s);
        return s;
    }

    private void setRecycler()
    {
        for(int i = 0;i<linearLayouts.length;i++){
            for(int j=0;j<data[i].length;j++){
                CheckBox c = new CheckBox(this);
                c.setText(data[i][j]);
                ((LinearLayout)findViewById(linearLayouts[i])).addView(c);
            }
        }
    }

    void getSelectedData(){

        services.clear();
        specialist.clear();
        insurance.clear();
        govScheme.clear();




        int checkedRadioButtonId = ((RadioGroup) findViewById(R.id.radioRating)).getCheckedRadioButtonId();
        Log.d("selected value"," "+checkedRadioButtonId);
        rating = ((RadioButton)findViewById(checkedRadioButtonId)).getText().toString().toLowerCase();
        checkedRadioButtonId = ((RadioGroup) findViewById(R.id.radioBudget)).getCheckedRadioButtonId();
        budget = ((RadioButton)findViewById(checkedRadioButtonId)).getText().toString().toLowerCase();




        LinearLayout viewById = (LinearLayout) findViewById(linearLayouts[0]);
        for(int i=0;i<viewById.getChildCount();i++){
            if(((CheckBox)viewById.getChildAt(i)).isChecked()){
                services.add(data[0][i].toLowerCase());
            }
        }

        viewById = (LinearLayout) findViewById(linearLayouts[1]);
        for(int i=0;i<viewById.getChildCount();i++){
            if(((CheckBox)viewById.getChildAt(i)).isChecked()){
                specialist.add(data[1][i].toLowerCase());
            }
        }


        viewById = (LinearLayout) findViewById(linearLayouts[2]);
        for(int i=0;i<viewById.getChildCount();i++){
            if(((CheckBox)viewById.getChildAt(i)).isChecked()){
                insurance.add(data[2][i].toLowerCase());
            }
        }


        viewById = (LinearLayout) findViewById(linearLayouts[3]);
        for(int i=0;i<viewById.getChildCount();i++){
            if(((CheckBox)viewById.getChildAt(i)).isChecked()){
                govScheme.add(schemeMap.get(data[3][i]).toLowerCase());
            }
        }

    }




}
