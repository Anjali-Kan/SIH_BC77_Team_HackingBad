package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import propya.mr.jeevan.Adapters.SchemeAdapter;
import propya.mr.jeevan.BluePrint.Scheme;
import propya.mr.jeevan.R;

public class SchemeFinder extends AppCompatActivity {
    ArrayList<Scheme> arrayOfSchemes = new ArrayList<Scheme>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scheme_finder);


        FirebaseDatabase.getInstance().getReference("emergencies/7hKhlTCQ5hpPitrtFmMz/schemeCodes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                int choice = (int) Math.floor(Math.random() * 8);
                for(DataSnapshot scheme : dataSnapshot.getChildren()){
                    String linkingStatus = "Not Linked";
                    if(count<choice)
                        linkingStatus = "Linked";
                    count++;
                    arrayOfSchemes.add(new Scheme(scheme.child("name").getValue(String.class), linkingStatus,
                            scheme.child("link").getValue(String.class),
                            scheme.child("desc").getValue(String.class).replace("\\n","\n"),
                            scheme.child("what").getValue(String.class).replace("\\n","\n"),
                            scheme.child("coverage").getValue(String.class).replace("\\n","\n")));
                }

                SchemeAdapter adapter = new SchemeAdapter(SchemeFinder.this, arrayOfSchemes);
                ListView listView = (ListView) findViewById(R.id.schemeList);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener((adapterView, view, i, l) -> {
                    Scheme scheme = arrayOfSchemes.get(i);
                    Intent intent = new Intent(SchemeFinder.this, SchemeDetails.class);
                    intent.putExtra("Scheme", scheme);
                    startActivity(intent);
                });
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
