package propya.mr.jeevan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import propya.mr.jeevan.BluePrint.Scheme;
import propya.mr.jeevan.R;

public class SchemeDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheme_details);
        Intent intent = getIntent();
        Scheme scheme = intent.getParcelableExtra("Scheme");

        TextView name = findViewById(R.id.schemeName);
        name.setText(scheme.getmSchemeName());

        TextView status = findViewById(R.id.schemeStatus);
        status.setText(scheme.getmSchemeStatus());

        TextView desc = findViewById(R.id.schemeDesc);
        desc.setText(scheme.getmSchemeDesc());

        TextView what = findViewById(R.id.schemeWhat);
        what.setText(scheme.getmSchemeWhat());

        TextView cov = findViewById(R.id.schemeCoverage);
        cov.setText(scheme.getmSchemeCoverage());

        if(scheme.getmSchemeStatus().equals("Linked")) {
            status.setTextColor(Color.parseColor("#1ed950"));
        }
        else {
            status.setTextColor(Color.parseColor("#c20404"));
        }

        Button knowMore = findViewById(R.id.knowMore);
        knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = scheme.getmSchemeLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}