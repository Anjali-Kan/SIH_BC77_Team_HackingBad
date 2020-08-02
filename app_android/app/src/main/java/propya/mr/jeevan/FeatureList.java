package propya.mr.jeevan;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.lang.reflect.Constructor;

import butterknife.BindView;
import propya.mr.jeevan.Activities.ChooseEmergencyActivity;
import propya.mr.jeevan.Activities.KnowTheHospital;
import propya.mr.jeevan.Activities.SchemeFinder;

public class FeatureList extends ActivityHelper {

    @BindView(R.id.center_linear)
    LinearLayout rootLinear;

    @Override
    protected void viewReady(View v) {
        addFeature(ChooseEmergencyActivity.class);
        addFeature(KnowTheHospital.class);
        addFeature(SchemeFinder.class);
    }

    void addFeature(String name, Intent i){
        addFeature(name, v -> startActivity(i));
    }

    void addFeature(String name, View.OnClickListener i){
            Button b = new Button(this);
            b.setText(name);
            b.setOnClickListener(i);
            rootLinear.addView(b);
    }

    void addFeature(Class c){
        addFeature(c.getSimpleName(),new Intent(this,c));
    }


    @Override
    protected int getRootView() {
        return R.layout.generic_centre_linear;
    }
}
