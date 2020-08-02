package propya.mr.jeevan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.ButterKnife;

public abstract class BottomFragHelper extends BottomSheetDialogFragment {

    View rootView,internalView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.frag_helper, container, false);
        LinearLayout rootLayout = rootView.findViewById(R.id.fragHelperRoot);
        internalView =  inflater.inflate(getLayoutId(), rootLayout, false);
        rootLayout.addView(internalView);
        ButterKnife.bind(this,internalView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        layoutReady(internalView);
    }

    protected abstract int getLayoutId();
    protected abstract void layoutReady(View view);

}
