package propya.mr.jeevan.UI_HELPERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

import propya.mr.jeevan.Activities.KnowTheHospital;
import propya.mr.jeevan.R;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {


    Context c;
    HashMap<String,ArrayList<String>> details;
    boolean isCheckBox;
    ArrayList<String> heads = new ArrayList<>();
    KnowTheHospital hospital = new KnowTheHospital();

    public FilterAdapter(Context c, HashMap<String, ArrayList<String>> details, boolean isCheckBox) {
        this.c = c;
        this.details = details;
        this.isCheckBox = isCheckBox;
        heads.addAll(details.keySet());
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(c).inflate(R.layout.adapter_filters,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.imageView.setImageDrawable(c.getDrawable(hospital.drawables[position]));
        for(String s:details.get(heads.get(position))){
            RadioButton button = new RadioButton(c);
            button.setText(s);
            holder.layout.addView(button);
        }
    }

    @Override
    public int getItemCount() {
        return heads.size();
    }


    public class FilterViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        LinearLayout layout;

        public FilterViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView= itemView.findViewById(R.id.non_expanded_filter);
            layout = itemView.findViewById(R.id.linearLayoutFilter);
        }
    }

    public interface ElementChecked
    {
        public void AddElement(String root,String child);
        public void RemoveElement(String root,String child);

    }



}
