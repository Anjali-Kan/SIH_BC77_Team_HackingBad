package propya.mr.jeevan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;


public class SchemeHelper extends BottomFragHelper {

    @BindView(R.id.schemeHead)
    TextView t;
    public String headName;

    public SchemeHelper() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scheme_helper;
    }

    @Override
    protected void layoutReady(View view) {
        t.setText(headName);
    }
}