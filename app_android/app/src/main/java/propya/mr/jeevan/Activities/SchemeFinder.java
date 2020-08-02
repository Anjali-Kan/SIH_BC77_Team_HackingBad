package propya.mr.jeevan.Activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import propya.mr.jeevan.BluePrint.Scheme;
import propya.mr.jeevan.R;
import propya.mr.jeevan.UI_HELPERS.SchemeAdapter;

public class SchemeFinder extends AppCompatActivity {
    ArrayList<Scheme> arrayOfSchemes = new ArrayList<Scheme>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scheme_finder);


        FirebaseDatabase.getInstance().getReference("emergencies/7hKhlTCQ5hpPitrtFmMz/schemeCodes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot schemes : dataSnapshot.getChildren()){
                    arrayOfSchemes.add(new Scheme(schemes.child("name").getValue(String.class),"not linked"));
                }

                SchemeAdapter adapter = new SchemeAdapter(SchemeFinder.this, arrayOfSchemes);
                ListView listView = (ListView) findViewById(R.id.schemeList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        arrayOfSchemes.add(new Scheme("Scheme 2","not linked"));
//        arrayOfSchemes.add(new Scheme("Scheme 3","linked"));
//        arrayOfSchemes.add(new Scheme("Scheme 4","not linked"));
// Create the adapter to convert the array to views
// Attach the adapter to a ListView

    }
}
